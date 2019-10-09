package com.nerdstone.neatformcore.views.builders

import android.support.design.widget.TextInputEditText
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.widgets.DateTimePickerNFormView
import java.util.*

class DateTimePickerViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private val dateTimePickerNFormView = nFormView as DateTimePickerNFormView
    private val textInputEditText = TextInputEditText(dateTimePickerNFormView.context)
    override val acceptedAttributes = Utils.convertEnumToSet(DateTimePickerProperties::class.java)

    enum class DateTimePickerProperties {
        HINT
    }

    override fun buildView() {
        ViewUtils.applyViewAttributes(
            nFormView = dateTimePickerNFormView,
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )
        dateTimePickerNFormView.addView(textInputEditText)
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        textInputEditText.apply {
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                DateTimePickerProperties.HINT.name -> {
                    hint = attribute.value.toString()
                }
            }
        }
    }
}