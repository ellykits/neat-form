package com.nerdstone.neatformcore.rules

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.model.NFormFieldValidation
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.*
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
 */
class NeatFormValidator : FormValidator {

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

        val viewDetails = nFormView.viewDetails

        if (viewDetails.view.visibility == View.VISIBLE) {
            if ((viewDetails.value == null || viewDetails.value is HashMap<*, *>
                        && (viewDetails.value as HashMap<*, *>).isEmpty())
                && nFormView.isFieldRequired()
            ) {
                invalidFields.add(viewDetails.name)
                val errorMessage =
                        nFormView.viewProperties.requiredStatus?.extractKeyValue()?.second
                return Pair(false, errorMessage)
            }
            if (nFormView.viewProperties.validations != null) {
                nFormView.viewProperties.validations?.forEach { validation ->
                    if (!performValidation(validation, viewDetails)) {
                        invalidFields.add(viewDetails.name)
                        return Pair(false, validation.message)
                    }
                }
            }
        }
        invalidFields.remove(viewDetails.name)
        requiredFields.remove(viewDetails.name)
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
            labelTextView.apply { error = validationPair.second }
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
     * @param viewDetails details about the current view
     * @return true if validation passes false otherwise
     */
    private fun performValidation(validation: NFormFieldValidation, viewDetails: NFormViewDetails): Boolean {
        facts.put(VALIDATION_RESULT, false)
        val dataViewModel:DataViewModel = viewDetails.getDataViewModel()
        facts.asMap().putAll(getFormData(dataViewModel))
        facts.put(VALUE, viewDetails.value)

        val customRule: Rule = MVELRule()
            .name(UUID.randomUUID().toString())
            .description(validation.condition)
            .`when`(validation.condition)
            .then("$VALIDATION_RESULT = true")

        val rules = Rules(customRule)
        rulesEngine.fire(rules, facts)

        return facts.get(VALIDATION_RESULT)
    }

    private fun getFormData(dataViewModel: DataViewModel): MutableMap<String, Any?> {
        return dataViewModel.details.value?.mapValuesTo(mutableMapOf(), { entry -> entry.value })!!
    }
}