package com.nerdstone.neatformcore.views.builders

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.model.NFormSubViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.getViewsByTagValue
import com.nerdstone.neatformcore.views.containers.RadioGroupView
import java.util.*

class RadioGroupViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private val radioGroupView = nFormView as RadioGroupView

    enum class RadioGroupViewProperties {
        TEXT
    }

    override val acceptedAttributes get() = Utils.convertEnumToSet(RadioGroupViewProperties::class.java)


    override fun buildView() {
        ViewUtils.applyViewAttributes(
            nFormView = radioGroupView,
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )
        createMultipleRadios()
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        when (attribute.key.toUpperCase(Locale.getDefault())) {
            RadioGroupViewProperties.TEXT.name -> {
                createLabel(attribute)
            }
        }
    }

    private fun createLabel(attribute: Map.Entry<String, Any>) {
        val label = TextView(radioGroupView.context)
        label.apply {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTextAppearance(R.style.labelStyle)
            else setTextAppearance(
                radioGroupView.context,
                R.style.labelStyle
            )

            layoutParams = ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            text = attribute.value.toString()

            if (radioGroupView.viewProperties.requiredStatus != null
                && Utils.isFieldRequired(radioGroupView)
            ) {
                text = ViewUtils.addRedAsteriskSuffix(label.text.toString())
            }
        }
        radioGroupView.addView(label)
    }


    private fun createMultipleRadios() {
        val options = radioGroupView.viewProperties.options
        options?.also {
            options.forEach { createSingleRadioButton(it) }
        }
    }

    private fun createSingleRadioButton(nFormSubViewProperty: NFormSubViewProperty) {
        val radioButton = RadioButton(radioGroupView.context)
        radioButton.apply {
            text = nFormSubViewProperty.text
            setTag(R.id.field_name, nFormSubViewProperty.name)
            setTag(R.id.is_radio_group_option, true)
            setOnCheckedChangeListener { radioButton, isChecked ->
                if (isChecked) {
                    radioGroupView.viewDetails.value =
                        mutableMapOf(radioGroupView.viewDetails.name to radioButton.text.toString())
                    handleExclusiveChecks(this, radioButton.getTag(R.id.field_name) as String)
                    radioGroupView.dataActionListener?.onPassData(radioGroupView.viewDetails)
                }

            }
        }

        radioGroupView.addView(radioButton)
    }

    /**
     * You can only select one radio button at a time
     */
    private fun handleExclusiveChecks(radioButton: RadioButton, selectedOption: String) {
        val radioButtons =
            (radioButton.parent as View).getViewsByTagValue(R.id.is_radio_group_option, true)

        radioButtons.forEach { view ->
            if (view is RadioButton && view.getTag(R.id.field_name) as String != selectedOption &&
                view.getTag(R.id.is_radio_group_option) != null && view.getTag(R.id.is_radio_group_option) == true
            ) {
                view.isChecked = false
            }
        }
    }

    fun resetRadioButtonsValue() {
        val radioButtons =
            (radioGroupView as View).getViewsByTagValue(R.id.is_radio_group_option, true)
        radioButtons.forEach { view ->
            if (view is RadioButton && view.isChecked) {
                view.isChecked = false
            }
        }
        radioGroupView.viewDetails.value = null
        radioGroupView.dataActionListener?.onPassData(radioGroupView.viewDetails)
    }
}


