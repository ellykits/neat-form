package com.nerdstone.neatformcore.views.builders

import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.widget.LinearLayout
import com.google.android.material.textfield.TextInputEditText
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.removeAsterisk
import com.nerdstone.neatformcore.views.widgets.TextInputEditTextNFormView
import java.util.*


class TextInputEditTextBuilder(override val nFormView: NFormView) : ViewBuilder {
    private val textInputEditTextNFormView = nFormView as TextInputEditTextNFormView

    enum class TextInputEditTextViewProperties {
        HINT, PADDING, TEXT_SIZE, TEXT
    }

    override val acceptedAttributes get() = Utils.convertEnumToSet(TextInputEditTextViewProperties::class.java)


    override fun buildView() {
        createEditText()
        ViewUtils.applyViewAttributes(
            nFormView = textInputEditTextNFormView,
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        textInputEditTextNFormView.apply {
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                TextInputEditTextViewProperties.PADDING.name -> {
                    val value = Utils.pxToDp(
                        (attribute.value as String).toFloat(),
                        textInputEditTextNFormView.context
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
                    requestFocus()
                    textInputEditTextNFormView.dataActionListener?.onPassData(
                        textInputEditTextNFormView.viewDetails
                    )
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
            if (!hasFocus) {
                Utils.hideSoftKeyBoard(textInputEditText)
            }
        }

        textInputEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //Implement
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Implement
            }

            override fun onTextChanged(
                text: CharSequence, start: Int, lengthBefore: Int,
                lengthAfter: Int
            ) {
                if (text.isNotEmpty()) {
                    textInputEditTextNFormView.dataActionListener?.also {
                        textInputEditTextNFormView.viewDetails.value =
                            text.toString().removeAsterisk()
                        it.onPassData(textInputEditTextNFormView.viewDetails)
                    }
                }
            }
        })
    }

    private fun formatHintForRequiredFields() {
        if (textInputEditTextNFormView.viewProperties.requiredStatus != null) {
            if (Utils.isFieldRequired(textInputEditTextNFormView)) {
                textInputEditTextNFormView.hint =
                    ViewUtils.addRedAsteriskSuffix(textInputEditTextNFormView.hint.toString())
            }
        }
    }
}


