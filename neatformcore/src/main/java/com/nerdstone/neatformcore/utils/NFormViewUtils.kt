package com.nerdstone.neatformcore.utils

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.AppCompatEditText
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.Constants.ViewType
import com.nerdstone.neatformcore.views.controls.EditTextNFormView
import com.nerdstone.neatformcore.views.controls.SpinnerNFormView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import java.util.*

object NFormViewUtils {

    fun createViews(
        rootView: RootView, viewProperties: List<NFormViewProperty>,
        context: Context, viewDispatcher: ViewDispatcher
    ) {

        for (viewProperty in viewProperties) {
            viewDispatcher.rulesFactory
                .registerSubjects(splitText(viewProperty.subjects, ","), viewProperty)

            when (viewProperty.type) {
                ViewType.EDIT_TEXT ->
                    rootView.addChild(
                        EditTextNFormView(context).initView(
                            viewProperty,
                            viewDispatcher
                        )
                    )
                ViewType.SPINNER -> rootView.addChild(
                    SpinnerNFormView(context).initView(
                        viewProperty,
                        viewDispatcher
                    )
                )
            }
        }
    }

    fun splitText(text: String?, delimiter: String): List<String> {
        return if (text == null || text.isEmpty()) {
            ArrayList()
        } else listOf(*text.split(delimiter.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
    }

    fun addRedAsterixOnHint(editText: AppCompatEditText) {
        if ((editText.hint.isNotBlank())) {
            val hint = SpannableString(editText.hint.toString() + " *")
            hint.setSpan(
                ForegroundColorSpan(Color.RED), hint.length - 1, hint.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            editText.hint = hint
        }
    }
}
