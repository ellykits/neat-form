package com.nerdstone.neatformcore.views.builders

import android.view.View
import android.widget.CheckBox
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.model.NFormSubViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.getViewsByTagValue
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import java.util.*

class MultiChoiceCheckBoxViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private val multiChoiceCheckBox = nFormView as MultiChoiceCheckBox
    private val valuesMap = mutableMapOf<String, String?>()

    enum class MultiChoiceCheckBoxProperties {
        TEXT
    }

    override val acceptedAttributes get() = Utils.convertEnumToSet(MultiChoiceCheckBoxProperties::class.java)


    override fun buildView() {
        ViewUtils.applyViewAttributes(
            nFormView = multiChoiceCheckBox,
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )
        createMultipleCheckboxes()
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        when (attribute.key.toUpperCase(Locale.getDefault())) {
            MultiChoiceCheckBoxProperties.TEXT.name -> {
                multiChoiceCheckBox.addView(
                    ViewUtils.addViewLabel(attribute.toPair(), multiChoiceCheckBox)
                )
            }
        }
    }

    private fun createMultipleCheckboxes() {
        multiChoiceCheckBox.viewProperties.options
            ?.also { option ->
                option.forEach { createSingleCheckBox(it) }
            }
    }

    private fun createSingleCheckBox(nFormSubViewProperty: NFormSubViewProperty) {
        val checkBox = CheckBox(multiChoiceCheckBox.context)
        checkBox.apply {
            text = nFormSubViewProperty.text
            setTag(R.id.field_name, nFormSubViewProperty.name)
            setTag(R.id.is_checkbox_option, true)
            ViewUtils.applyCheckBoxStyle(multiChoiceCheckBox.context, checkBox)
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    multiChoiceCheckBox.viewDetails.value =
                        mutableMapOf(buttonView.getTag(R.id.field_name) to buttonView.text.toString())
                    handleExclusiveChecks(this)
                } else {
                    multiChoiceCheckBox.viewDetails.value =
                        mutableMapOf(buttonView.getTag(R.id.field_name) to null)
                }
                handleCheckBoxValues(this)
            }

            if (nFormSubViewProperty.isExclusive != null && nFormSubViewProperty.isExclusive == true) {
                setTag(R.id.is_exclusive_checkbox, true)
            }
        }
        multiChoiceCheckBox.addView(checkBox)
    }

    /**
     * Save the name of the checkbox option plus the label as key-value pair when the checkbox
     * is selected
     */
    private fun handleCheckBoxValues(checkBox: CheckBox) {
        val nameTag = checkBox.getTag(R.id.field_name) as String
        if (checkBox.isChecked) {
            valuesMap[nameTag] = checkBox.text.toString()
        } else {
            valuesMap[nameTag] = null
        }

        multiChoiceCheckBox.viewDetails.value = valuesMap
        multiChoiceCheckBox.dataActionListener?.onPassData(multiChoiceCheckBox.viewDetails)
    }

    /**
     * Example is when you have an option called  'none' and you do not want select it with the
     * other options.
     */
    private fun handleExclusiveChecks(checkBox: CheckBox) {
        val isExclusive = checkBox.getTag(R.id.is_exclusive_checkbox) as Boolean?
        val checkBoxes = (checkBox.parent as View).getViewsByTagValue(R.id.is_checkbox_option, true)

        when (isExclusive) {
            null, false -> checkBoxes.forEach { view ->
                if (view is CheckBox && view.getTag(R.id.is_exclusive_checkbox) != null &&
                    view.getTag(R.id.is_exclusive_checkbox) == true
                ) {
                    view.isChecked = false
                }
            }
            else -> checkBoxes.forEach { view ->
                if (view is CheckBox && view.getTag(R.id.field_name) != checkBox.getTag(R.id.field_name) && isExclusive == true) {
                    view.isChecked = false
                }
            }
        }
    }

    fun resetCheckBoxValues() {
        (multiChoiceCheckBox as View).getViewsByTagValue(R.id.is_checkbox_option, true)
            .map { it as CheckBox }
            .forEach { view ->
                if (view.isChecked) {
                    view.isChecked = false
                }
            }
    }
}


