package com.nerdstone.neatformcore.views.builders

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.model.NFormSubViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.getViewsByTagValue
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox

class MultiChoiceCheckBoxViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private val multiChoiceCheckBox = nFormView as MultiChoiceCheckBox
    private val valuesMap = mutableMapOf<String, String?>()

    enum class MultiChoiceCheckBoxProperties {
        TEXT
    }

    override val acceptedAttributes: HashSet<String>
        get() = Utils.convertEnumToSet(MultiChoiceCheckBoxProperties::class.java)


    override fun buildView() {
        ViewUtils.applyViewAttributes(
            nFormView = multiChoiceCheckBox,
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )
        createMultipleCheckboxes()
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        when (attribute.key.toUpperCase()) {
            MultiChoiceCheckBoxProperties.TEXT.name -> {
                createLabel(attribute)
            }
        }
    }

    private fun createLabel(attribute: Map.Entry<String, Any>) {
        val label = TextView(multiChoiceCheckBox.context)
        label.apply {

            layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            text = attribute.value.toString()

            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> setTextAppearance(R.style.multiChoiceCheckBoxLabelStyle)
                else -> setTextAppearance(
                    multiChoiceCheckBox.context,
                    R.style.multiChoiceCheckBoxLabelStyle
                )
            }

            if (multiChoiceCheckBox.viewProperties.requiredStatus != null
                && Utils.isFieldRequired(multiChoiceCheckBox)
            ) {
                text = ViewUtils.addRedAsteriskSuffix(label.text.toString())
            }
        }
        multiChoiceCheckBox.addView(label)
    }


    private fun createMultipleCheckboxes() {
        val options = multiChoiceCheckBox.viewProperties.options
        options?.also {
            options.forEach { createSingleCheckBox(it) }
        }
    }

    private fun createSingleCheckBox(nFormSubViewProperty: NFormSubViewProperty) {
        val checkBox = CheckBox(multiChoiceCheckBox.context)
        checkBox.apply {
            text = nFormSubViewProperty.text
            setTag(R.id.field_name, nFormSubViewProperty.name)
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
            setTag(R.id.is_checkbox_option, true)
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
        val checkBoxes =
            ((multiChoiceCheckBox) as View).getViewsByTagValue(R.id.is_checkbox_option, true)
        checkBoxes.forEach { view ->
            if (view is CheckBox && view.isChecked) {
                view.isChecked = false
            }
        }
    }
}


