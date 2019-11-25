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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.Constants.ViewType
import com.nerdstone.neatformcore.viewmodel.DataViewModel
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import com.nerdstone.neatformcore.views.containers.RadioGroupView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.widgets.*
import java.util.*
import kotlin.reflect.KClass

const val ID = "id"

object ViewUtils {

    fun createViews(
        rootView: RootView, viewProperties: List<NFormViewProperty>,
        viewDispatcher: ViewDispatcher, buildFromLayout: Boolean = false
    ) {

        for (viewProperty in viewProperties) {
            when (viewProperty.type) {
                ViewType.EDIT_TEXT ->
                    buildView(
                        buildFromLayout, rootView, viewProperty, viewDispatcher,
                        EditTextNFormView::class
                    )
                ViewType.MULTI_CHOICE_CHECKBOX ->
                    buildView(
                        buildFromLayout, rootView, viewProperty, viewDispatcher,
                        MultiChoiceCheckBox::class
                    )
                ViewType.CHECKBOX ->
                    buildView(
                        buildFromLayout, rootView, viewProperty, viewDispatcher,
                        CheckBoxNFormView::class
                    )
                ViewType.SPINNER ->
                    buildView(
                        buildFromLayout, rootView, viewProperty, viewDispatcher,
                        SpinnerNFormView::class
                    )
                ViewType.RADIO_GROUP ->
                    buildView(
                        buildFromLayout, rootView, viewProperty, viewDispatcher,
                        RadioGroupView::class
                    )
                ViewType.DATETIME_PICKER ->
                    buildView(
                        buildFromLayout, rootView, viewProperty, viewDispatcher,
                        DateTimePickerNFormView::class
                    )
                ViewType.NUMBER_SELECTOR ->
                    buildView(
                        buildFromLayout, rootView, viewProperty, viewDispatcher,
                        NumberSelectorNFormView::class
                    )
                ViewType.TEXT_INPUT_EDIT_TEXT ->
                    buildView(
                        buildFromLayout, rootView, viewProperty, viewDispatcher,
                        TextInputEditTextNFormView::class
                    )
            }

        }
    }

    private fun <T : NFormView> buildView(
        buildFromLayout: Boolean, rootView: RootView,
        viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher, kClass: KClass<T>
    ) {
        val androidView = rootView as View
        val context = rootView.context
        val viewModel =
            ViewModelProviders.of(context as FragmentActivity)[DataViewModel::class.java]
        val view: NFormView
        if (buildFromLayout) {
            view = androidView.findViewById<View>(
                context.resources.getIdentifier(viewProperty.name, ID, context.packageName)
            ) as NFormView
            getView(view, viewProperty, viewDispatcher)
        } else {
            val objectConstructor = kClass.constructors.minBy { it.parameters.size }
            view = objectConstructor!!.call(context)
            rootView.addChild(
                getView(view, viewProperty, viewDispatcher)
            )
        }
        viewModel.values[view.viewProperties.name] = view.viewDetails
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
