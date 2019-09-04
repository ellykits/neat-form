package com.nerdstone.neatformcore.builders

import android.text.SpannableStringBuilder
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.utils.NFormViewUtils
import com.nerdstone.neatformcore.utils.Utils
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
                            addRedAsteriskOnHint(editTextNFormView)
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

    private fun addRedAsteriskOnHint(editTextNFormView: EditTextNFormView) {
        if (Utils.extractKeyValue(editTextNFormView.viewProperties.requiredStatus!!)
                .first == "yes"
        ) {
            NFormViewUtils.addRedAsterixOnHint(editTextNFormView)
        }
    }

}