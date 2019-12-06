package com.nerdstone.neatformcore.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
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
import com.nerdstone.neatformcore.rules.NeatFormValidator
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.widgets.CheckBoxNFormView
import com.nerdstone.neatformcore.views.widgets.DateTimePickerNFormView
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import com.nerdstone.neatformcore.views.widgets.NumberSelectorNFormView
import com.nerdstone.neatformcore.views.widgets.SpinnerNFormView
import com.nerdstone.neatformcore.views.widgets.TextInputEditTextNFormView
import java.util.*
import kotlin.reflect.KClass

const val ID = "id"
const val VALUE = "value"
const val VALIDATION_RESULT = "validationResults"

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
        if (buildFromLayout) {
            val view = androidView.findViewById<View>(
                context.resources.getIdentifier(viewProperty.name, ID, context.packageName)
            )
            getView(view as NFormView, viewProperty, viewDispatcher)
        } else {
            val objectConstructor = kClass.constructors.minBy { it.parameters.size }
            rootView.addChild(
                getView(objectConstructor!!.call(context), viewProperty, viewDispatcher)
            )
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
        nFormView.viewDetails.view.tag = viewProperty.name
        nFormView.dataActionListener = viewDispatcher
        addRequiredFields(nFormView)
        nFormView.trackRequiredField()
        nFormView.viewBuilder.buildView()
    }

    private fun addRequiredFields(nFormView: NFormView) {
        if (Utils.isFieldRequired(nFormView))
            nFormView.formValidator.requiredFields.add(nFormView.viewDetails.name)
    }

    fun applyViewAttributes(
        nFormView: NFormView, acceptedAttributes: HashSet<String>,
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

    fun addViewLabel(attribute: Pair<String, Any>, nFormView: NFormView): LinearLayout {
        val layoutInflater =
            (nFormView as View).context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = layoutInflater.inflate(R.layout.custom_label_layout, null) as LinearLayout

        layout.findViewById<TextView>(R.id.labelTextView).apply {

            text = attribute.second as String

            if (Utils.isFieldRequired(nFormView)) {
                text = addRedAsteriskSuffix(text.toString())
            }
            error = null
        }
        (nFormView as View).findViewById<TextView>(R.id.errorMessageTextView)?.visibility =
            View.GONE
        return layout
    }

    fun handleRequiredStatus(nFormView: NFormView) {
        (nFormView as View).tag?.also {
            val formValidator = nFormView.formValidator
            if (Utils.isFieldRequired(nFormView) &&
                nFormView.viewDetails.value == null &&
                nFormView.viewDetails.view.visibility == View.VISIBLE
            ) {
                formValidator.requiredFields.add(nFormView.viewDetails.name)
            } else {
                formValidator.invalidFields.remove(it)
                formValidator.requiredFields.remove(it)
            }
        }
    }

    fun animateView(view: View) {
        when (view.visibility) {
            View.VISIBLE -> {
                view.animate()
                    .alpha(1.0f)
                    .duration = 800
            }
            View.GONE -> {
                view.animate()
                    .alpha(0.0f)
                    .duration = 800
            }
        }
    }
}