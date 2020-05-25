package com.nerdstone.neatformcore.utils

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.nerdstone.neatformcore.BuildConfig
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import timber.log.Timber
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
        val registeredViews = rootView.formBuilder.registeredViews
        for (viewProperty in viewProperties) {
            buildView(
                buildFromLayout, rootView, viewProperty, viewDispatcher,
                registeredViews[viewProperty.type] as KClass<*>
            )
        }
    }

    private fun buildView(
        buildFromLayout: Boolean, rootView: RootView,
        viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher, kClass: KClass<*>
    ) {
        val androidView = rootView as View
        val context = rootView.context
        if (buildFromLayout) {
            val view = androidView.findViewById<View>(
                context.resources.getIdentifier(viewProperty.name, ID, context.packageName)
            )
            try {
                getView(view as NFormView, viewProperty, viewDispatcher)
            } catch (e: Exception) {
                Timber.e(e)
                if (BuildConfig.DEBUG)
                    Toast.makeText(
                        rootView.context, "ERROR: The view with name ${viewProperty.name} " +
                                "defined in json form is missing in custom layout", LENGTH_LONG
                    ).show()
            }
        } else {
            val constructor = kClass.constructors.minBy { it.parameters.size }
            rootView.addChild(
                getView(constructor!!.call(context) as NFormView, viewProperty, viewDispatcher)
            )
        }
    }

    private fun getView(
        nFormView: NFormView, viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher
    ): NFormView {
        if (viewProperty.subjects != null) {
            viewDispatcher.rulesFactory
                .registerSubjects(splitText(viewProperty.subjects, ","), viewProperty)
            val hasVisibilityRule =
                viewDispatcher.rulesFactory.viewHasVisibilityRule(viewProperty)
            if (hasVisibilityRule) {
                viewDispatcher.rulesFactory.rulesHandler.changeVisibility(
                    false,
                    nFormView.viewDetails.view
                )
            }
        }
        return setupView(nFormView, viewProperty, viewDispatcher)
    }

    fun splitText(text: String?, delimiter: String): List<String> {
        return if (text == null || text.isEmpty()) {
            ArrayList()
        } else listOf(*text.split(delimiter.toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray())
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
        nFormView: NFormView, viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher
    ): NFormView {
        with(nFormView) {
            //Set view properties
            viewProperties = viewProperty
            viewDetails.name = viewProperty.name
            viewDetails.metadata = viewProperty.viewMetadata

            //Add listener and build view
            viewDetails.view.id = View.generateViewId()
            viewDetails.view.tag = viewProperty.name
            dataActionListener = viewDispatcher
            addRequiredFields(nFormView)
            trackRequiredField()
            viewBuilder.buildView()
        }
        return nFormView
    }

    private fun addRequiredFields(nFormView: NFormView) {
        if (Utils.isFieldRequired(nFormView))
            nFormView.formValidator.requiredFields.add(nFormView.viewDetails.name)
    }

    fun applyViewAttributes(
        nFormView: NFormView, acceptedAttributes: HashSet<String>,
        task: (attribute: Map.Entry<String, Any>) -> Unit
    ) {
        nFormView.viewProperties.viewAttributes?.forEach { attribute ->
            if (acceptedAttributes.contains(attribute.key.toUpperCase(Locale.getDefault()))) {
                task(attribute)
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
        val layout = View.inflate((nFormView as View).context, R.layout.custom_label_layout, null)
                as LinearLayout

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
                formValidator.requiredFields.remove(it)
            }
        }
    }

    fun animateView(view: View) {
        when (view.visibility) {
            View.VISIBLE -> view.animate().alpha(1.0f).duration = 800
            View.GONE -> view.animate().alpha(0.0f).duration = 800
        }
    }


    /**
     * Crediting @see <a href='https://stackoverflow.com/questions/2586301/set-inputtype-for-an-edittext-programmatically?rq=1'>
     *     StackOverflow Implement InputType on EditText</a>
     */
    internal fun getSupportedEditTextTypes() =
        mapOf(
            "date" to InputTypeItem(InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_DATE),
            "datetime" to InputTypeItem(InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_NORMAL),
            "none" to InputTypeItem(InputType.TYPE_NULL),
            "number" to InputTypeItem(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL),
            "numberDecimal" to InputTypeItem(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL),
            "numberPassword" to InputTypeItem(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD),
            "numberSigned" to InputTypeItem(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED),
            "phone" to InputTypeItem(InputType.TYPE_CLASS_PHONE),
            "text" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL),
            "textAutoComplete" to InputTypeItem(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE),
            "textAutoCorrect" to InputTypeItem(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT),
            "textCapCharacters" to InputTypeItem(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS),
            "textCapSentences" to InputTypeItem(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES),
            "textCapWords" to InputTypeItem(InputType.TYPE_TEXT_FLAG_CAP_WORDS),
            "textEmailAddress" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS),
            "textEmailSubject" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT),
            "textFilter" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_FILTER),
            "textLongMessage" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE),
            "textMultiLine" to InputTypeItem(InputType.TYPE_TEXT_FLAG_MULTI_LINE),
            "textNoSuggestions" to InputTypeItem(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS),
            "textPassword" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD),
            "textPersonName" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME),
            "textPhonetic" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PHONETIC),
            "textPostalAddress" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS),
            "textShortMessage" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE),
            "textUri" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI),
            "textVisiblePassword" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD),
            "textWebEditText" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT),
            "textWebEmailAddress" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS),
            "textWebPassword" to InputTypeItem(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD),
            "time" to InputTypeItem(InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_TIME)
        )

}

internal data class InputTypeItem(val inputType: Int)
