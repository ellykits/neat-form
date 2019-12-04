package com.nerdstone.neatformcore.form.json

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NFormFieldValidation
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.VALIDATION_RESULT
import com.nerdstone.neatformcore.utils.VALUE
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.DefaultRulesEngine
import org.jeasy.rules.mvel.MVELRule
import java.util.*

class JsonFormValidator private constructor(override var formBuilder: FormBuilder) : FormValidator {

    override fun validateField(nFormView: NFormView): Pair<Boolean, String?> {
        if (nFormView.viewProperties.validations != null) {
            nFormView.viewProperties.validations?.forEach { validation ->
                if (!performValidation(validation, nFormView.viewDetails.value))
                    return Pair(false, validation.message)
            }
        }
        return Pair(true, "")
    }

    override fun validateLabeledField(nFormView: NFormView): Boolean {
        val validationPair = validateField(nFormView)
        val anchorView = nFormView.viewDetails.view as ViewGroup
        val labelTextView =
            (anchorView.getChildAt(0) as LinearLayout).findViewById<TextView>(R.id.labelTextView)
        if (!validationPair.first) {
            labelTextView.apply {
                error = validationPair.second
            }
            showErrorMessage(anchorView, validationPair.second)
        } else {
            labelTextView.error = null
            (anchorView.getChildAt(0) as LinearLayout).findViewById<TextView>(R.id.errorMessageTextView)
                .visibility =
                View.GONE
        }
        return validationPair.first
    }

    private fun performValidation(validation: NFormFieldValidation, value: Any?): Boolean {

        val facts = Facts()
        facts.put(VALUE, value)
        facts.put(VALIDATION_RESULT, false)

        // define rules
        val customRule: Rule = MVELRule()
            .name(UUID.randomUUID().toString())
            .description(validation.condition)
            .`when`(validation.condition)
            .then("$VALIDATION_RESULT = true")

        val rules = Rules(customRule)

        // fire rules on known facts
        val rulesEngine = DefaultRulesEngine()
        rulesEngine.fire(rules, facts)

        if (facts.get<Boolean>(VALIDATION_RESULT)) return true
        return false
    }

    private fun showErrorMessage(anchorView: View, errorMessage: String?) {
        anchorView.findViewById<TextView>(R.id.errorMessageTextView).apply {
            text = errorMessage
            visibility = View.VISIBLE
        }
    }

    companion object {
        @Volatile
        private var instance: JsonFormValidator? = null

        fun getInstance(formBuilder: FormBuilder): JsonFormValidator =
            instance ?: synchronized(this) {
                JsonFormValidator(formBuilder).also {
                    instance = it
                }
            }
    }
}