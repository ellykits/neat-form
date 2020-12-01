package com.nerdstone.neatformcore.views.builders

import androidx.core.widget.TextViewCompat
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.*
import com.nerdstone.neatformcore.views.widgets.CheckBoxNFormView
import java.util.*

open class CheckBoxViewBuilder(final override val nFormView: NFormView) : ViewBuilder {

    private val checkBoxNFormView = nFormView as CheckBoxNFormView

    override val acceptedAttributes = CheckBoxProperties::class.java.convertEnumToSet()

    override lateinit var resourcesMap: MutableMap<String, Int>

    enum class CheckBoxProperties {
        TEXT, TEXT_SIZE
    }

    override fun buildView() {
        checkBoxNFormView.applyViewAttributes(acceptedAttributes, this::setViewProperties)
        with(checkBoxNFormView) {
            if (!viewProperties.requiredStatus.isNullOrBlank() && isFieldRequired()) {
                text = text.toString().addRedAsteriskSuffix()
            }
        }
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        checkBoxNFormView.apply {
            if (viewProperties.getResourceFromAttribute().isNullOrEmpty())
                TextViewCompat.setTextAppearance(checkBoxNFormView, R.style.checkBoxStyle)
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