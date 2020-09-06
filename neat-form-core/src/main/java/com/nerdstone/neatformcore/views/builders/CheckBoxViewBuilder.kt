package com.nerdstone.neatformcore.views.builders

import androidx.core.widget.TextViewCompat
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.getStyleFromAttribute
import com.nerdstone.neatformcore.views.widgets.CheckBoxNFormView
import java.util.*

open class CheckBoxViewBuilder(final override val nFormView: NFormView) : ViewBuilder {

    private val checkBoxNFormView = nFormView as CheckBoxNFormView

    override val acceptedAttributes = Utils.convertEnumToSet(CheckBoxProperties::class.java)

    override lateinit var stylesMap: MutableMap<String, Int>

    enum class CheckBoxProperties {
        TEXT, TEXT_SIZE
    }

    override fun buildView() {
        ViewUtils.applyViewAttributes(checkBoxNFormView, acceptedAttributes, this::setViewProperties)

       with(checkBoxNFormView.viewProperties) {
           if (requiredStatus != null && Utils.isFieldRequired(checkBoxNFormView)) {
               checkBoxNFormView.text =
                       ViewUtils.addRedAsteriskSuffix(checkBoxNFormView.text.toString())
           }
           getStyleFromAttribute()?.let { applyStyle(it) }
       }
    }

    override fun applyStyle(style: String) {
        stylesMap[style]?.let { TextViewCompat.setTextAppearance(checkBoxNFormView, it) }
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        checkBoxNFormView.apply {
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