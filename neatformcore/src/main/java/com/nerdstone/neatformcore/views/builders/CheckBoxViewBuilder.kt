package com.nerdstone.neatformcore.views.builders

import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.views.controls.CheckBoxNFormView

class CheckBoxViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private val checkBoxNFormView: CheckBoxNFormView = nFormView as CheckBoxNFormView

    override val acceptedAttributes: HashSet<String> =
        Utils.convertEnumToSet(CheckBoxProperties::class.java)

    enum class CheckBoxProperties {
        TEXT
    }

    override fun buildView() {
        checkBoxNFormView.viewProperties.viewAttributes?.forEach { attribute ->
            if (acceptedAttributes.contains(attribute.key.toUpperCase())) {
                checkBoxNFormView.apply {
                    when (attribute.key.toUpperCase()) {
                        CheckBoxProperties.TEXT.name -> {
                            text = attribute.value.toString()
                        }
                    }
                }
            }
        }
    }
}