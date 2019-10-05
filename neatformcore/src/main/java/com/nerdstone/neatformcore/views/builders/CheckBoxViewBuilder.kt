package com.nerdstone.neatformcore.views.builders

import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.widgets.CheckBoxNFormView

class CheckBoxViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private val checkBoxNFormView: CheckBoxNFormView = nFormView as CheckBoxNFormView

    override val acceptedAttributes: HashSet<String> =
        Utils.convertEnumToSet(CheckBoxProperties::class.java)

    enum class CheckBoxProperties {
        TEXT
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
            when (attribute.key.toUpperCase()) {
                CheckBoxProperties.TEXT.name -> {
                    text = attribute.value.toString()
                }
            }
            ViewUtils.applyCheckBoxStyle(checkBoxNFormView.context, checkBoxNFormView)
        }
    }
}