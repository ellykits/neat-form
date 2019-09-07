package com.nerdstone.neatformcore.views.builders

import android.text.SpannableStringBuilder
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.controls.EditTextNFormView
import java.util.*

class EditTextViewBuilder(private val editTextNFormView: EditTextNFormView) : ViewBuilder {

    enum class EditTextProperties {
        HINT, PADDING, TEXT_SIZE
    }

    override val acceptedAttributes: HashSet<String> =
        Utils.convertEnumToSet(EditTextProperties::class.java)


    override fun buildView() {
        editTextNFormView.viewProperties.viewAttributes?.forEach { attribute ->
            if (acceptedAttributes.contains(attribute.key.toUpperCase())) {
                editTextNFormView.apply {
                    when (attribute.key.toUpperCase()) {
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

                    }
                }
            }
        }
    }

    private fun formatHintForRequiredFields(editTextNFormView: EditTextNFormView) {
        if (editTextNFormView.viewProperties.requiredStatus != null) {
            val isRequired =
                Utils.extractKeyValue(editTextNFormView.viewProperties.requiredStatus!!)
                    .first.toLowerCase()
            if (isRequired == "yes" || isRequired == "true") {
                ViewUtils.appendRedAsteriskToHint(editTextNFormView)
            }
        }
    }
}