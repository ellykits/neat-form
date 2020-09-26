package com.nerdstone.neatformcore.views.builders

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.model.NFormSubViewProperty
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.*
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import java.util.*

open class MultiChoiceCheckBoxViewBuilder(final override val nFormView: NFormView) : ViewBuilder {

    private val multiChoiceCheckBox = nFormView as MultiChoiceCheckBox
    private var checkBoxTextSize: Float? = null
    private var valuesMap: HashMap<String, NFormViewData?>? = null

    enum class MultiChoiceCheckBoxProperties {
        TEXT, OPTIONS_TEXT_SIZE, LABEL_TEXT_SIZE
    }

    override val acceptedAttributes get() = MultiChoiceCheckBoxProperties::class.java.convertEnumToSet()

    override lateinit var resourcesMap: MutableMap<String, Int>

    override fun buildView() {
        multiChoiceCheckBox.applyViewAttributes(
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )
        createMultipleCheckboxes()
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        when (attribute.key.toUpperCase(Locale.getDefault())) {
            MultiChoiceCheckBoxProperties.TEXT.name -> {
                multiChoiceCheckBox.addView(multiChoiceCheckBox.addViewLabel(attribute.toPair()))
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
            if (multiChoiceCheckBox.viewProperties.getResourceFromAttribute().isNullOrEmpty())
                TextViewCompat.setTextAppearance(this, R.style.checkBoxStyle)
            setOnCheckedChangeListener { compoundButton, isChecked ->
                if (valuesMap == null) valuesMap = hashMapOf()
                val fieldName = compoundButton.getTag(R.id.field_name)
                if (isChecked) {
                    valuesMap?.put(
                        fieldName as String,
                        NFormViewData(
                            null, compoundButton.text.toString(),
                            multiChoiceCheckBox.getOptionMetadata(fieldName)
                        )
                    )
                    handleExclusiveChecks(this)
                } else {
                    valuesMap?.remove(fieldName as String)
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
     * Example is when you have an option called  'none' and you do not want to select it with the
     * other options.
     */
    private fun handleExclusiveChecks(checkBox: CheckBox) {
        val isExclusive = checkBox.getTag(R.id.is_exclusive_checkbox) as Boolean?
        val checkBoxes = (checkBox.parent as View).getViewsByTagValue(R.id.is_checkbox_option, true)
        when (isExclusive) {
            null, false -> checkBoxes.forEach { view ->
                val exclusiveTag = view.getTag(R.id.is_exclusive_checkbox)
                if (view is CheckBox && exclusiveTag != null && exclusiveTag == true)
                    view.isChecked = false
            }
            else -> checkBoxes.forEach { view ->
                val fieldTag = view.getTag(R.id.field_name)
                if (view is CheckBox && fieldTag != checkBox.getTag(R.id.field_name) && isExclusive == true)
                    view.isChecked = false
            }
        }
    }

    fun resetCheckBoxValues() {
        (multiChoiceCheckBox as View).getViewsByTagValue(R.id.is_checkbox_option, true)
            .map { it as CheckBox }
            .forEach { view -> if (view.isChecked) view.isChecked = false }
        valuesMap = null
        multiChoiceCheckBox.viewDetails.value = valuesMap
        multiChoiceCheckBox.dataActionListener?.onPassData(multiChoiceCheckBox.viewDetails)
    }

    fun setValue(selectedOptions: Set<Any?>, enabled: Boolean) {
        (multiChoiceCheckBox as View).getViewsByTagValue(R.id.is_checkbox_option, true)
            .map { it as CheckBox }
            .forEach { view ->
                if (selectedOptions.contains(view.getTag(R.id.field_name))) view.isChecked = true
                view.setReadOnlyState(enabled)
            }
    }
}


