package com.nerdstone.neatformcore.views.builders

import android.os.Build
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
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox

class MultiChoiceCheckBoxViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private val multiChoiceCheckBox = nFormView as MultiChoiceCheckBox

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
        }
        multiChoiceCheckBox.addView(checkBox)
    }
}