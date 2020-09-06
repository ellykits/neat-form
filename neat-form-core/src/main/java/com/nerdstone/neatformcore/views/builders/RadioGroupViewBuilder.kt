package com.nerdstone.neatformcore.views.builders

import android.view.View
import android.widget.RadioButton
import androidx.core.widget.TextViewCompat
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.model.NFormSubViewProperty
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.ViewUtils.setReadOnlyState
import com.nerdstone.neatformcore.utils.getViewsByTagValue
import com.nerdstone.neatformcore.views.containers.RadioGroupView
import java.util.*

open class RadioGroupViewBuilder(final override val nFormView: NFormView) : ViewBuilder {

    private val radioGroupView = nFormView as RadioGroupView

    enum class RadioGroupViewProperties {
        TEXT
    }

    override val acceptedAttributes get() = Utils.convertEnumToSet(RadioGroupViewProperties::class.java)

    override lateinit var stylesMap: MutableMap<String, Int>

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
                radioGroupView.addView(ViewUtils.addViewLabel(attribute.toPair(), radioGroupView))
            }
        }
    }

    override fun applyStyle(style: String) {
        TODO("Not yet implemented")
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
            TextViewCompat.setTextAppearance(this, R.style.radioButtonStyle)
            setTag(R.id.field_name, nFormSubViewProperty.name)
            setTag(R.id.is_radio_group_option, true)
            setOnCheckedChangeListener { radioButton, isChecked ->
                if (isChecked) {
                    radioGroupView.viewDetails.value =
                        mutableMapOf(
                            radioButton.getTag(R.id.field_name) to
                                    NFormViewData(
                                        null, radioButton.text.toString(),
                                        Utils.getOptionMetadata(
                                            radioGroupView,
                                            radioButton.getTag(R.id.field_name) as String
                                        )
                                    )
                        )
                    handleExclusiveChecks(this, radioButton.getTag(R.id.field_name) as String)
                    radioGroupView.dataActionListener?.onPassData(radioGroupView.viewDetails)
                }
            }
            radioGroupView.addView(this)
        }
    }

    /**
     * You can only select one radio button at a time
     */
    private fun handleExclusiveChecks(radioButton: RadioButton, selectedOption: String) {
        (radioButton.parent as View).getViewsByTagValue(R.id.is_radio_group_option, true)
            .map { it as RadioButton }
            .forEach { view ->
                val optionTag = view.getTag(R.id.is_radio_group_option)
                if (view.getTag(R.id.field_name) as String != selectedOption
                    && optionTag != null && optionTag == true
                ) view.isChecked = false
            }
    }

    fun resetRadioButtonsValue() {
        (radioGroupView as View).getViewsByTagValue(R.id.is_radio_group_option, true)
            .map { it as RadioButton }
            .forEach { view ->
                if (view.isChecked) view.isChecked = false
            }
        radioGroupView.viewDetails.value = null
        radioGroupView.dataActionListener?.onPassData(radioGroupView.viewDetails)
    }

    fun setValue(selectedOption: String, enabled: Boolean) {
        for (view in radioGroupView.getViewsByTagValue(R.id.is_radio_group_option, true)
            .map { it as RadioButton }) {
            val optionTag = view.getTag(R.id.is_radio_group_option)
            if (view.getTag(R.id.field_name) as String == selectedOption
                && optionTag != null && optionTag == true
            ) view.isChecked = true
            view.setReadOnlyState(enabled)
        }
    }
}


