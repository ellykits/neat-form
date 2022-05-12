package com.nerdstone.neatformcore.views.builders

import android.text.SpannableStringBuilder
import androidx.core.widget.TextViewCompat
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.*
import com.nerdstone.neatformcore.views.widgets.MaskedEditTextNFormView
import java.util.*

open class MaskedEditTextViewBuilder(final override val nFormView: NFormView) : ViewBuilder {

    private val editTextNFormView = nFormView as MaskedEditTextNFormView

    override val acceptedAttributes = EditTextProperties::class.java.convertEnumToSet()

    private val acceptedMaskedAttributes = MaskedEditTextProperties::class.java.convertEnumToSet()

    override lateinit var resourcesMap: MutableMap<String, Int>

    enum class MaskedEditTextProperties {
        MASK,  MASK_HINT
    }

    enum class EditTextProperties {
        HINT, PADDING, TEXT_SIZE, TEXT, INPUT_TYPE, BACKGROUND, ALLOWED_CHARS,
    }

    override fun buildView() {
        //set MaskedProperties before the EditTextProperties
        if (!acceptedMaskedAttributes.isNullOrEmpty()) {
            editTextNFormView.apply {
                applyViewAttributes(
                    acceptedMaskedAttributes,
                    this@MaskedEditTextViewBuilder::setMaskedProperties
                )
                setOnFocusChangeListener { _, hasFocus ->
                    if (!hasFocus) {
                        editTextNFormView.hideSoftKeyBoard()
                    }
                }
            }
        }
        editTextNFormView.apply {
            applyViewAttributes(
                acceptedAttributes,
                this@MaskedEditTextViewBuilder::setViewProperties
            )
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
                floatingLabelText = floatingLabelText.toString().addRedAsteriskSuffix()
            }
        }
    }

    private fun setMaskedProperties(attribute: Map.Entry<String, Any>) {
        editTextNFormView.apply {
            if (viewProperties.getResourceFromAttribute().isNullOrEmpty())
                TextViewCompat.setTextAppearance(editTextNFormView, R.style.editTextStyle)
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                MaskedEditTextProperties.MASK_HINT.name -> {
                    hint = SpannableStringBuilder(attribute.value as String)
                }
                MaskedEditTextProperties.MASK.name -> {
                    mask = attribute.value.toString()
                }

            }
        }
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        editTextNFormView.apply {
            if (viewProperties.getResourceFromAttribute().isNullOrEmpty())
                TextViewCompat.setTextAppearance(editTextNFormView, R.style.editTextStyle)
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                EditTextProperties.ALLOWED_CHARS.name -> {
                    allowedChars = attribute.value.toString()
                }
                EditTextProperties.HINT.name -> {
                    floatingLabelText = attribute.value.toString()
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
                        ?.also { setRawInputType(it) }
                }
                EditTextProperties.BACKGROUND.name -> {
                    resourcesMap[attribute.value.toString()]?.let { setBackgroundResource(it) }
                }

            }
        }
    }
}