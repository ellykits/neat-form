package com.nerdstone.neatformcore.views.builders

import android.os.Build
import android.text.SpannableStringBuilder
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import java.util.*


class EditTextViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private val editTextNFormView = nFormView as EditTextNFormView

    override val acceptedAttributes = Utils.convertEnumToSet(EditTextProperties::class.java)

    enum class EditTextProperties {
        HINT, PADDING, TEXT_SIZE, TEXT
    }

    override fun buildView() {
        ViewUtils.applyViewAttributes(
            nFormView = editTextNFormView,
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )
        //Hide keyboard when focus is lost
        editTextNFormView.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                Utils.hideSoftKeyBoard(editTextNFormView)
            }
        }
    }

    private fun formatHintForRequiredFields(editTextNFormView: EditTextNFormView) {
        if (editTextNFormView.viewProperties.requiredStatus != null) {
            if (Utils.isFieldRequired(editTextNFormView)) {
                editTextNFormView.hint =
                    ViewUtils.addRedAsteriskSuffix(editTextNFormView.hint.toString())
            }
        }
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        editTextNFormView.apply {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTextAppearance(R.style.editTextStyle)
            else setTextAppearance(editTextNFormView.context, R.style.editTextStyle)

            when (attribute.key.toUpperCase(Locale.getDefault())) {
                EditTextProperties.HINT.name -> {
                    hint = SpannableStringBuilder(attribute.value as String)
                    formatHintForRequiredFields(editTextNFormView)
                }

                EditTextProperties.PADDING.name -> {
                    val value = Utils.pxToDp(
                        (attribute.value as String).toFloat(),
                        editTextNFormView.context
                    )
                    setPadding(value, value, value, value)
                }

                EditTextProperties.TEXT_SIZE.name ->
                    textSize = (attribute.value as String).toFloat()

                EditTextProperties.TEXT.name -> {
                    setText(attribute.value.toString())
                    requestFocus()
                    editTextNFormView.dataActionListener?.onPassData(editTextNFormView.viewDetails)
                }
            }
        }
    }
}