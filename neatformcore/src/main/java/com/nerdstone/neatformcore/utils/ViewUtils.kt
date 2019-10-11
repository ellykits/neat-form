package com.nerdstone.neatformcore.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.CheckBox
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.Constants.ViewType
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import com.nerdstone.neatformcore.views.containers.RadioGroupView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.widgets.CheckBoxNFormView
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import com.nerdstone.neatformcore.views.widgets.NotesNFormView
import com.nerdstone.neatformcore.views.widgets.SpinnerNFormView
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
                ViewType.TOASTERS_NOTES ->
                    rootView.addChild(
                            NotesNFormView(context).initView(viewProperty, viewDispatcher)
                    )
                ViewType.SPINNER ->
                    rootView.addChild(
                        SpinnerNFormView(context).initView(viewProperty, viewDispatcher)
                    )
                ViewType.RADIO_GROUP ->
                    rootView.addChild(
                        RadioGroupView(context).initView(viewProperty, viewDispatcher)
                    )
            }
        }
    }

    fun splitText(text: String?, delimiter: String): List<String> {
        return if (text == null || text.isEmpty()) {
            ArrayList()
        } else listOf(*text.split(delimiter.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
    }

    fun addRedAsteriskSuffix(text: String): SpannableString {
        if (text.isNotEmpty()) {
            val textWithSuffix = SpannableString("$text *")
            textWithSuffix.setSpan(
                    ForegroundColorSpan(Color.RED), textWithSuffix.length - 1, textWithSuffix.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            return textWithSuffix
        }
        return SpannableString(text)
    }

    fun getKey(name: String, suffix: String): String {
        return name.substringBefore(suffix)
    }

    fun setupView(
            nFormView: NFormView, viewProperty: NFormViewProperty,
            viewDispatcher: ViewDispatcher
    ) {
        //Set view properties
        nFormView.viewProperties = viewProperty
        nFormView.viewDetails.name = viewProperty.name
        nFormView.viewDetails.metadata = viewProperty.viewMetadata
        nFormView.viewDetails.subjects = splitText(viewProperty.subjects, ",")

        //Add listener and build view
        nFormView.viewDetails.view.id = View.generateViewId()
        viewDispatcher.rulesFactory.rulesHandler.viewIdsMap[viewProperty.name] =
            nFormView.viewDetails.view.id
        nFormView.dataActionListener = viewDispatcher
        nFormView.viewBuilder.buildView()
    }

    fun applyViewAttributes(
            nFormView: NFormView,
            acceptedAttributes: HashSet<String>,
            task: (attribute: Map.Entry<String, Any>) -> Unit
    ) {
        if (nFormView.viewProperties.viewAttributes != null) {
            nFormView.viewProperties.viewAttributes?.forEach { attribute ->
                if (acceptedAttributes.contains(attribute.key.toUpperCase(Locale.getDefault()))) {
                    task(attribute)
                }
            }
        }
    }

    fun applyCheckBoxStyle(context: Context, checkBox: CheckBox) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> checkBox.setTextAppearance(R.style.checkBoxStyle)
            else -> checkBox.setTextAppearance(context, R.style.checkBoxStyle)
        }
    }
}
