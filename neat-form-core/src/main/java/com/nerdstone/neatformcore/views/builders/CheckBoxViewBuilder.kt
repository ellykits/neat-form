package com.nerdstone.neatformcore.views.builders

import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.widgets.CheckBoxNFormView
import java.util.*

open class CheckBoxViewBuilder(final override val nFormView: NFormView) : ViewBuilder {

    private val checkBoxNFormView = nFormView as CheckBoxNFormView

    override val acceptedAttributes = Utils.convertEnumToSet(CheckBoxProperties::class.java)

    enum class CheckBoxProperties {
        TEXT, TEXT_SIZE
    }

    override fun buildView() {
        ViewUtils.applyViewAttributes(
            nFormView = checkBoxNFormView,
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )

        if (checkBoxNFormView.viewProperties.requiredStatus != null
            && Utils.isFieldRequired(checkBoxNFormView)
        ) {
            checkBoxNFormView.text =
                ViewUtils.addRedAsteriskSuffix(checkBoxNFormView.text.toString())
        }
    }


    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        checkBoxNFormView.apply {
            ViewUtils.applyCheckBoxStyle(checkBoxNFormView.context, checkBoxNFormView)
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                CheckBoxProperties.TEXT.name -> {
                    text = attribute.value.toString()
                }

                CheckBoxProperties.TEXT_SIZE.name -> {
                    textSize = (attribute.value.toString()).toFloat()
                }
            }
        }
    }
}