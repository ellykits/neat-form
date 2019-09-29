package com.nerdstone.neatformcore.utils

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.AppCompatEditText
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.Constants.ViewType
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import com.nerdstone.neatformcore.views.controls.CheckBoxNFormView
import com.nerdstone.neatformcore.views.controls.EditTextNFormView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import java.util.*

object ViewUtils {

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
                        EditTextNFormView(context).initView(viewProperty, viewDispatcher)
                    )
                ViewType.MULTI_CHOICE_CHECKBOX ->
                    rootView.addChild(
                        MultiChoiceCheckBox(context).initView(viewProperty, viewDispatcher)
                    )
                ViewType.CHECKBOX ->
                    rootView.addChild(
                        CheckBoxNFormView(context).initView(viewProperty, viewDispatcher)
                    )
            }
        }
    }

    fun splitText(text: String?, delimiter: String): List<String> {
        return if (text == null || text.isEmpty()) {
            ArrayList()
        } else listOf(*text.split(delimiter.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
    }

    fun appendRedAsteriskToHint(editText: AppCompatEditText) {
        if (editText.hint.isNotBlank()) {
            val hint = SpannableString(editText.hint.toString() + " *")
            hint.setSpan(
                ForegroundColorSpan(Color.RED), hint.length - 1, hint.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            editText.hint = hint
        }
    }

    fun getKey(name: String, suffix: String): String {
        return name.substringBefore(suffix)
    }

    fun setupView(
        nFormView: NFormView, viewProperty: NFormViewProperty, viewBuilder: ViewBuilder,
        viewDispatcher: ViewDispatcher
    ): NFormView {
        nFormView.viewProperties = viewProperty
        nFormView.viewDetails.name = viewProperty.name
        nFormView.viewDetails.metadata = viewProperty.viewMetadata
        nFormView.viewDetails.subjects = splitText(viewProperty.subjects, ",")
        nFormView.mapViewIdToName(viewDispatcher.rulesFactory.rulesHandler)
        nFormView.setOnDataPassListener(viewDispatcher)
        viewBuilder.buildView()
        return nFormView
    }
}
