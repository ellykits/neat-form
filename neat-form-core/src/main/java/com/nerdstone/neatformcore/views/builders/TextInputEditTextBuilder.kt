package com.nerdstone.neatformcore.views.builders

import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.*
import com.nerdstone.neatformcore.views.widgets.TextInputEditTextNFormView
import java.util.*

open class TextInputEditTextBuilder(final override val nFormView: NFormView) : ViewBuilder {

    private val textInputEditTextNFormView = nFormView as TextInputEditTextNFormView

    enum class TextInputEditTextViewProperties {
        HINT, PADDING, TEXT_SIZE, TEXT, INPUT_TYPE
    }

    override val acceptedAttributes get() = TextInputEditTextViewProperties::class.java.convertEnumToSet()

    override lateinit var resourcesMap: MutableMap<String, Int>

    override fun buildView() {
        createEditText()
        textInputEditTextNFormView.applyViewAttributes(acceptedAttributes, this::setViewProperties)
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        textInputEditTextNFormView.apply {
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                TextInputEditTextViewProperties.PADDING.name -> {
                    val value = textInputEditTextNFormView.context.pxToDp(
                        (attribute.value as String).toFloat(),
                    )
                    setPadding(value, value, value, value)
                }
                TextInputEditTextViewProperties.HINT.name -> {
                    hint = SpannableStringBuilder(attribute.value as String)
                    formatHintForRequiredFields()
                }

            }
        }

        textInputEditTextNFormView.editText?.apply {
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                TextInputEditTextViewProperties.TEXT_SIZE.name ->
                    textSize = (attribute.value as String).toFloat()

                TextInputEditTextViewProperties.TEXT.name -> {
                    setText(attribute.value.toString())
                }
                TextInputEditTextViewProperties.INPUT_TYPE.name -> {
                    getSupportedEditTextTypes()[attribute.value.toString()]
                        ?.also { inputType = it }
                }
            }
        }
    }

    private fun createEditText() {
        var textInputEditText = TextInputEditText(textInputEditTextNFormView.context)

        val editTextParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        if (textInputEditTextNFormView.editText == null)
            textInputEditTextNFormView.addView(textInputEditText, editTextParams)
        else
            textInputEditText = textInputEditTextNFormView.editText as TextInputEditText

        //Hide keyboard when focus is lost
        textInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) textInputEditText.hideSoftKeyBoard()
        }

        textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) = Unit

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(
                text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int
            ) {
                textInputEditTextNFormView.dataActionListener?.also {
                    if (text.isNotEmpty()) {
                        textInputEditTextNFormView.viewDetails.value =
                            text.toString().removeAsterisk()

                    } else {
                        textInputEditTextNFormView.viewDetails.value = null
                    }
                    it.onPassData(textInputEditTextNFormView.viewDetails)
                }
            }
        })
    }

    private fun formatHintForRequiredFields() {
        with(textInputEditTextNFormView) {
            if (!viewProperties.requiredStatus.isNullOrBlank() && isFieldRequired()) {
                hint = hint.toString().addRedAsteriskSuffix()
            }
        }
    }
}


