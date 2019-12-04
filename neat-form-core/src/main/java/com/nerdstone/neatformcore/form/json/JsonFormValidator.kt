package com.nerdstone.neatformcore.form.json

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NFormFieldValidation
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.VALIDATION_RESULT
import com.nerdstone.neatformcore.utils.VALUE
import com.nerdstone.neatformcore.viewmodel.DataViewModel
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.DefaultRulesEngine
import org.jeasy.rules.mvel.MVELRule
import java.util.*

/***
 * @author Elly Nerdstone
 * This class is used to handle form validations
 * @property formBuilder Form builder that validation is performed on
 */
class JsonFormValidator (override var formBuilder: FormBuilder) : FormValidator {

    private val rulesEngine = DefaultRulesEngine()
    private val facts = Facts()

    override fun validateField(nFormView: NFormView): Pair<Boolean, String?> {
        if (nFormView.viewProperties.validations != null) {
            nFormView.viewProperties.validations?.forEach { validation ->
                if (!performValidation(validation, nFormView.viewDetails.value))
                    return Pair(false, validation.message)
            }
        }
        return Pair(true, "")
    }

    /***
     * Validates views with Labels i.e RadioButtons, MultiChoiceCheckbox, NumberSelector
     * @param nFormView View to be validated
     * @return true if validation is successful false otherwise
     */
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

    private fun showErrorMessage(anchorView: View, errorMessage: String?) {
        anchorView.findViewById<TextView>(R.id.errorMessageTextView).apply {
            text = errorMessage
            visibility = View.VISIBLE
        }
    }

    /**
     * @param validation Validation to run
     * @param value value used to run the validation against
     * @return true if validation passes false otherwise
     */
    private fun performValidation(validation: NFormFieldValidation, value: Any?): Boolean {
        value?.also {
            facts.put(VALIDATION_RESULT, false)
            facts.asMap().putAll(getFormData())
            facts.put(VALUE, value)

            val customRule: Rule = MVELRule()
                .name(UUID.randomUUID().toString())
                .description(validation.condition)
                .`when`(validation.condition)
                .then("$VALIDATION_RESULT = true")

            val rules = Rules(customRule)
            rulesEngine.fire(rules, facts)
            return facts.get<Boolean>(VALIDATION_RESULT)
        }
        return true
    }

    private fun getFormData(): MutableMap<String, Any?> {
        val viewModel =
            ViewModelProviders.of(formBuilder.context as FragmentActivity)[DataViewModel::class.java]
        return viewModel.details.mapValuesTo(mutableMapOf(), { entry ->
            entry.value.value
        })
    }
}