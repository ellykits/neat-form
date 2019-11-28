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
import com.nerdstone.neatformcore.views.widgets.TextInputEditTextNFormView
import java.util.*


class TextInputEditTextBuilder(override val nFormView: NFormView) : ViewBuilder {
    private val textInputLayout = nFormView as TextInputEditTextNFormView

    enum class TextInputEditTextViewProperties {
        HINT, PADDING, TEXT_SIZE, TEXT
    }

    override val acceptedAttributes get() = Utils.convertEnumToSet(TextInputEditTextViewProperties::class.java)


    override fun buildView() {
        createEditText()
        ViewUtils.applyViewAttributes(
            nFormView = textInputLayout,
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        textInputLayout.apply {
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                TextInputEditTextViewProperties.PADDING.name -> {
                    val value = Utils.pxToDp(
                        (attribute.value as String).toFloat(),
                        textInputLayout.context
                    )
                    setPadding(value, value, value, value)
                }

                TextInputEditTextViewProperties.HINT.name -> {
                    hint = SpannableStringBuilder(attribute.value as String)
                    formatHintForRequiredFields()
                }

            }
        }

        textInputLayout.editText?.apply {
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                TextInputEditTextViewProperties.TEXT_SIZE.name ->
                    textSize = (attribute.value as String).toFloat()

                TextInputEditTextViewProperties.TEXT.name -> {
                    setText(attribute.value.toString())
                    requestFocus()
                    textInputLayout.dataActionListener?.onPassData(textInputLayout.viewDetails)
                }
            }
        }
    }

    private fun createEditText() {
        var textInputEditTextNFormView =
            TextInputEditText(
                textInputLayout.context
            )

        val editTextParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        if (textInputLayout.editText == null)
            textInputLayout.addView(textInputEditTextNFormView, editTextParams);
        else
            textInputEditTextNFormView = textInputLayout.editText as TextInputEditText

        //Hide keyboard when focus is lost
        textInputEditTextNFormView.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                Utils.hideSoftKeyBoard(textInputEditTextNFormView)
            }
        }

        textInputEditTextNFormView.addTextChangedListener(object : TextWatcher {
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
                    textInputLayout.dataActionListener?.also {
                        textInputLayout.viewDetails.value = text.toString()
                        it.onPassData(textInputLayout.viewDetails)
                    }
                }
            }
        })


    }

    private fun formatHintForRequiredFields() {
        if (textInputLayout.viewProperties.requiredStatus != null) {
            if (Utils.isFieldRequired(textInputLayout)) {
                textInputLayout.hint =
                    ViewUtils.addRedAsteriskSuffix(textInputLayout.hint.toString())
            }
        }
    }
}


