package com.nerdstone.neatformcore.views.builders

import android.text.SpannableStringBuilder
import androidx.core.widget.TextViewCompat
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.*
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import java.util.*

open class EditTextViewBuilder(final override val nFormView: NFormView) : ViewBuilder {

    private val editTextNFormView = nFormView as EditTextNFormView

    override val acceptedAttributes = EditTextProperties::class.java.convertEnumToSet()

    override lateinit var resourcesMap: MutableMap<String, Int>

    enum class EditTextProperties {
        HINT, PADDING, TEXT_SIZE, TEXT, INPUT_TYPE, BACKGROUND
    }

    override fun buildView() {
        //Hide keyboard when focus is lost
        editTextNFormView.apply {
            applyViewAttributes(acceptedAttributes, this@EditTextViewBuilder::setViewProperties)
            setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    editTextNFormView.hideSoftKeyBoard()
                }
            }
        }
    }

    private fun formatHintForRequiredFields() {
        with(editTextNFormView) {
            if (!viewProperties.requiredStatus.isNullOrBlank() && isFieldRequired()) {
                hint = hint.toString().addRedAsteriskSuffix()
            }
        }
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        editTextNFormView.apply {
            if (viewProperties.getResourceFromAttribute().isNullOrEmpty())
                TextViewCompat.setTextAppearance(editTextNFormView, R.style.editTextStyle)

            when (attribute.key.toUpperCase(Locale.getDefault())) {
                EditTextProperties.HINT.name -> {
                    hint = SpannableStringBuilder(attribute.value as String)
                    formatHintForRequiredFields()
                }

                EditTextProperties.PADDING.name -> {
                    val value = editTextNFormView.context.pxToDp(
                        (attribute.value as String).toFloat(),
                    )
                    setPadding(value, value, value, value)
                }
                EditTextProperties.TEXT_SIZE.name ->
                    textSize = (attribute.value as String).toFloat()

                EditTextProperties.TEXT.name -> {
                    setText(attribute.value.toString())
                }
                EditTextProperties.INPUT_TYPE.name -> {
                    getSupportedEditTextTypes()[attribute.value.toString()]
                        ?.also { inputType = it }
                }
                EditTextProperties.BACKGROUND.name -> {
                    resourcesMap[attribute.value.toString()]?.let { setBackgroundResource(it) }
                }
            }
        }
    }
}