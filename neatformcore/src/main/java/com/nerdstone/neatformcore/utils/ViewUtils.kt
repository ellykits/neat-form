package com.nerdstone.neatformcore.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.Constants.ViewType
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import com.nerdstone.neatformcore.views.containers.RadioGroupView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.widgets.*
import java.util.*

object ViewUtils {

    fun createViews(
            rootView: RootView, viewProperties: List<NFormViewProperty>,
            context: Context, viewDispatcher: ViewDispatcher
    ) {

        for (viewProperty in viewProperties) {
            when (viewProperty.type) {
                ViewType.EDIT_TEXT ->
                    rootView.addChild(
                            getView(EditTextNFormView(context), viewProperty, viewDispatcher)
                    )
                ViewType.MULTI_CHOICE_CHECKBOX ->
                    rootView.addChild(
                            getView(MultiChoiceCheckBox(context), viewProperty, viewDispatcher)
                    )
                ViewType.CHECKBOX ->
                    rootView.addChild(
                            getView(CheckBoxNFormView(context), viewProperty, viewDispatcher)
                    )
                ViewType.SPINNER ->
                    rootView.addChild(
                            getView(SpinnerNFormView(context), viewProperty, viewDispatcher)
                    )
                ViewType.RADIO_GROUP ->
                    rootView.addChild(
                            getView(RadioGroupView(context), viewProperty, viewDispatcher)
                    )
                ViewType.DATETIME_PICKER ->
                    rootView.addChild(
                            getView(DateTimePickerNFormView(context), viewProperty, viewDispatcher)
                    )
                ViewType.NUMBER_SELECTOR ->
                    rootView.addChild(
                            getView(NumberSelectorNFormView(context), viewProperty, viewDispatcher)
                    )
            }

        }
    }

    fun updateViews(
            rootView: View, viewProperties: List<NFormViewProperty>,
            context: Context, viewDispatcher: ViewDispatcher
    ) {
        val packageName = context.packageName
        for (viewProperty in viewProperties) {
            when (viewProperty.type) {
                ViewType.EDIT_TEXT -> {
                    val v = rootView.findViewById<EditTextNFormView>(context.resources.getIdentifier(viewProperty.name, "id", packageName))
                    getView(v, viewProperty, viewDispatcher)
                }
                ViewType.MULTI_CHOICE_CHECKBOX -> {
                    val v = rootView.findViewById<MultiChoiceCheckBox>(context.resources.getIdentifier(viewProperty.name, "id", packageName))
                    getView(v, viewProperty, viewDispatcher)
                }

                ViewType.CHECKBOX -> {
                    val v = rootView.findViewById<CheckBoxNFormView>(context.resources.getIdentifier(viewProperty.name, "id", packageName))
                    getView(v, viewProperty, viewDispatcher)
                }

                ViewType.SPINNER -> {
                    val v = rootView.findViewById<SpinnerNFormView>(context.resources.getIdentifier(viewProperty.name, "id", packageName))
                    getView(v, viewProperty, viewDispatcher)
                }

                ViewType.RADIO_GROUP -> {
                    val v = rootView.findViewById<RadioGroupView>(context.resources.getIdentifier(viewProperty.name, "id", packageName))
                    getView(v, viewProperty, viewDispatcher)
                }
                ViewType.DATETIME_PICKER -> {
                    val v = rootView.findViewById<DateTimePickerNFormView>(context.resources.getIdentifier(viewProperty.name, "id", packageName))
                    getView(v, viewProperty, viewDispatcher)
                }

                ViewType.NUMBER_SELECTOR -> {
                    val v = rootView.findViewById<NumberSelectorNFormView>(context.resources.getIdentifier(viewProperty.name, "id", packageName))
                    getView(v, viewProperty, viewDispatcher)
                }
            }

        }
    }

    private fun getView(
            nFormView: NFormView, viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher
    ): NFormView {
        if (viewProperty.subjects != null) {
            viewDispatcher.rulesFactory
                    .registerSubjects(splitText(viewProperty.subjects, ","), viewProperty)
            val hasVisibilityRule = viewDispatcher.rulesFactory.viewHasVisibilityRule(
                    viewProperty
            )
            if (hasVisibilityRule) {
                viewDispatcher.rulesFactory.rulesHandler.changeVisibility(
                        false, nFormView.viewDetails.view
                )
            }
        }
        return nFormView.initView(viewProperty, viewDispatcher)
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

    fun addViewLabel(attribute: Pair<String, Any>, nFormView: NFormView): TextView {
        val label = TextView((nFormView as View).context)

        label.apply {
            setPadding(0, 0, 0, 16)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTextAppearance(R.style.labelStyle)
            else setTextAppearance((nFormView as View).context, R.style.labelStyle)

            layoutParams = ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )

            text = attribute.second as String

            if (nFormView.viewProperties.requiredStatus != null
                    && Utils.isFieldRequired(nFormView)
            ) {
                text = addRedAsteriskSuffix(label.text.toString())
            }
        }
        return label
    }
}
