package com.nerdstone.neatformcore.rules

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NFormFieldValidation
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
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
class NeatFormValidator private constructor() : FormValidator {

    override lateinit var formBuilder: FormBuilder
    private val rulesEngine = DefaultRulesEngine()
    private val facts = Facts()
    override val invalidFields = hashSetOf<String>()
    override val requiredFields = hashSetOf<String>()

    /***
     * Validates only visible fields
     * @param nFormView view to be validated
     * @return [Pair] of true if validation succeeds and a string of the error message
     */
    override fun validateField(nFormView: NFormView): Pair<Boolean, String?> {
        if (nFormView.viewDetails.view.visibility == View.VISIBLE) {
            if ((nFormView.viewDetails.value == null || nFormView.viewDetails.value is HashMap<*, *>
                            && (nFormView.viewDetails.value as HashMap<*, *>).isEmpty())
                    && Utils.isFieldRequired(nFormView)
            ) {
                invalidFields.add(nFormView.viewDetails.name)
                val errorMessage =
                        nFormView.viewProperties.requiredStatus?.let {
                            Utils.extractKeyValue(it).second
                        }
                return Pair(false, errorMessage)
            }
            if (nFormView.viewProperties.validations != null) {
                nFormView.viewProperties.validations?.forEach { validation ->
                    if (!performValidation(validation, nFormView.viewDetails.value)) {
                        invalidFields.add(nFormView.viewDetails.name)
                        return Pair(false, validation.message)
                    }
                }
            }
        }
        invalidFields.remove(nFormView.viewDetails.name)
        requiredFields.remove(nFormView.viewDetails.name)
        return Pair(true, null)
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
                    .visibility = View.GONE
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

        return facts.get(VALIDATION_RESULT)
    }

    private fun getFormData(): MutableMap<String, Any?> {
        val viewModel = ViewModelProvider(formBuilder.context as FragmentActivity)[DataViewModel::class.java]
        return viewModel.details.mapValuesTo(mutableMapOf(), { entry ->
            entry.value
        })
    }

    companion object {
        @Volatile
        private var instance: NeatFormValidator? = null

        val INSTANCE: NeatFormValidator
            get() = instance ?: synchronized(this) {
                NeatFormValidator().also {
                    instance = it
                }
            }
    }

}