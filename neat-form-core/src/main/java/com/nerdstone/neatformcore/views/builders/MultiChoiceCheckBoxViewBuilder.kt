package com.nerdstone.neatformcore.views.builders

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.model.NFormSubViewProperty
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.getViewsByTagValue
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import timber.log.Timber
import java.util.*

class MultiChoiceCheckBoxViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private val multiChoiceCheckBox = nFormView as MultiChoiceCheckBox
    private var checkBoxTextSize: Float? = null
    private var valuesMap: HashMap<String, NFormViewData?>? = null

    enum class MultiChoiceCheckBoxProperties {
        TEXT, OPTIONS_TEXT_SIZE,LABEL_TEXT_SIZE
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

            MultiChoiceCheckBoxProperties.LABEL_TEXT_SIZE.name -> {
                multiChoiceCheckBox.findViewById<TextView>(R.id.labelTextView)?.apply {
                    textSize = attribute.value.toString().toFloat()
                }
            }

            MultiChoiceCheckBoxProperties.OPTIONS_TEXT_SIZE.name -> {
                checkBoxTextSize = attribute.value.toString().toFloat()
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
            setOnCheckedChangeListener { compoundButton, isChecked ->
                if (valuesMap == null) {
                    valuesMap = hashMapOf()
                }
                if (isChecked) {
                    valuesMap?.put(
                        compoundButton.getTag(R.id.field_name) as String,
                        NFormViewData(
                            type = null,
                            value = compoundButton.text.toString(),
                            metadata = Utils.getOptionMetadata(
                                multiChoiceCheckBox,
                                compoundButton.getTag(R.id.field_name) as String
                            )
                        )
                    )
                    handleExclusiveChecks(this)
                } else {
                    valuesMap?.put(compoundButton.getTag(R.id.field_name) as String, null)
                }
                multiChoiceCheckBox.viewDetails.value = valuesMap
                multiChoiceCheckBox.dataActionListener?.onPassData(multiChoiceCheckBox.viewDetails)
            }

            if (nFormSubViewProperty.isExclusive != null && nFormSubViewProperty.isExclusive == true) {
                setTag(R.id.is_exclusive_checkbox, true)
            }

            checkBoxTextSize?.also { textSize = it }
        }
        multiChoiceCheckBox.addView(checkBox)
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
        valuesMap = null
        multiChoiceCheckBox.viewDetails.value = valuesMap
        multiChoiceCheckBox.dataActionListener?.onPassData(multiChoiceCheckBox.viewDetails)
    }
}


