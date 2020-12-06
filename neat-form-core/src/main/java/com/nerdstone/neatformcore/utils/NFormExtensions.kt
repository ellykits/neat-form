@file:JvmName("NeatFormUtils")

package com.nerdstone.neatformcore.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nerdstone.neatformcore.BuildConfig
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.form.json.JsonParser.parseJson
import com.nerdstone.neatformcore.viewmodel.DataViewModel
import timber.log.Timber
import java.util.*
import kotlin.reflect.KClass

/**
 * ======================================================================================================
 * The following are extension functions for:
 * [NFormView] and [RootView]
 * ======================================================================================================
 */
fun NFormViewProperty.getResourceFromAttribute(style: String = "style"): String? =
    viewAttributes?.get(style)?.toString()

fun RootView.createViews(
    viewProperties: List<NFormViewProperty>,
    formBuilder: FormBuilder, buildFromLayout: Boolean = false
) {
    val registeredViews = formBuilder.registeredViews
    for (viewProperty in viewProperties) {
        val kClass = registeredViews[viewProperty.type]
        if (kClass.isNotNull()) {
            buildView(buildFromLayout, viewProperty, formBuilder, kClass!!)
        }
    }
}

private fun RootView.buildView(
    buildFromLayout: Boolean, viewProperty: NFormViewProperty,
    formBuilder: FormBuilder, kClass: KClass<*>
) {
    val context = (this as View).context
    if (buildFromLayout) {
        val view = this.findViewById<View>(
            context.resources.getIdentifier(viewProperty.name, ID, context.packageName)
        )
        try {
            (view as NFormView).getView(viewProperty, formBuilder)
        } catch (e: Exception) {
            val message =
                "ERROR: The view with name ${viewProperty.name} defined in json form is missing in custom layout"
            Timber.e(e, message)
            if (BuildConfig.DEBUG) Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    } else {
        kClass.constructors.minByOrNull { it.parameters.size }?.also { constructors ->
            val nFormView =
                if (viewProperty.getResourceFromAttribute().isNullOrBlank())
                    constructors.call(context) as NFormView
                else
                    constructors.call(formBuilder.resourcesMap[viewProperty.getResourceFromAttribute()]
                        ?.let { ContextThemeWrapper(context, it) }) as NFormView
            addChild(nFormView.getView(viewProperty, formBuilder))
        }

    }
}

private fun NFormView.getView(
    viewProperty: NFormViewProperty, formBuilder: FormBuilder
): NFormView {
    if (viewProperty.subjects != null) {
        val viewDispatcher = formBuilder.viewDispatcher
        viewDispatcher.run {
            rulesFactory.registerSubjects(
                viewProperty.subjects.splitText(","), viewProperty
            )
            val hasVisibilityRule = rulesFactory.viewHasVisibilityRule(viewProperty)
            if (hasVisibilityRule) {
                rulesFactory.rulesHandler.changeVisibility(
                    false,
                    this@getView.viewDetails.view
                )
            }
        }
    }
    return setupView(viewProperty, formBuilder)
}

fun NFormView.setupView(
    viewProperty: NFormViewProperty, formBuilder: FormBuilder
): NFormView {
    with(this) {
        //Set late init properties
        viewProperties = viewProperty
        dataActionListener = formBuilder.viewDispatcher
        formValidator = formBuilder.formValidator

        with(viewDetails) {
            name = viewProperty.name
            metadata = viewProperty.viewMetadata
            view.id = View.generateViewId()
            view.tag = viewProperty.name
        }

        addRequiredFields()
        trackRequiredField()
        viewBuilder.resourcesMap = formBuilder.resourcesMap
        viewBuilder.buildView()
    }
    return this
}

private fun NFormView.addRequiredFields() {
    if (isFieldRequired())
        this.formValidator.requiredFields.add(this.viewDetails.name)
}

/**
 * This method is the one responsible for mapping the JSON string [acceptedAttributes] to actual
 * view attributes to [NFormView]. [task] is a the method that is responsible for applying these
 * properties
 */
fun NFormView.applyViewAttributes(
    acceptedAttributes: HashSet<String>, task: (attribute: Map.Entry<String, Any>) -> Unit
) {
    this.viewProperties.viewAttributes?.forEach { attribute ->
        if (acceptedAttributes.contains(attribute.key.toUpperCase(Locale.getDefault()))) {
            task(attribute)
        }
    }
}


/**
 * This method extracts the value from the provided [attribute] and uses it as the text that will
 * be displayed on the top label of [NFormView]
 */
fun NFormView.addViewLabel(attribute: Pair<String, Any>): LinearLayout {
    val layout = View.inflate((this as View).context, R.layout.custom_label_layout, null)
            as LinearLayout

    layout.findViewById<TextView>(R.id.labelTextView).apply {

        text = attribute.second as String

        if (isFieldRequired()) {
            text = text.toString().addRedAsteriskSuffix()
        }
        error = null
    }
    (this as View).findViewById<TextView>(R.id.errorMessageTextView)?.visibility = View.GONE
    return layout
}

/**
 * When a field represented by this [NFormView] is required it must be filled otherwise
 * data will not be valid and an empty map will be returned if you try to access the form data.
 */
fun NFormView.handleRequiredStatus() {
    val viewContext = viewDetails.getViewContext()
    val dataViewModel =
        ViewModelProvider(viewContext as FragmentActivity)[DataViewModel::class.java]
    (this as View).tag?.also {
        val formValidator = formValidator
        if (isFieldRequired() && viewDetails.value == null &&
            viewDetails.view.visibility == View.VISIBLE &&
            !dataViewModel.details.value?.containsKey(this.viewDetails.name)!!
        ) {
            formValidator.requiredFields.add(viewDetails.name)
        } else {
            formValidator.requiredFields.remove(it)
        }
    }
}

fun NFormView.isFieldRequired(): Boolean {
    viewProperties.requiredStatus?.also {
        val isRequired = viewProperties.requiredStatus!!.extractKeyValue()
            .first.toLowerCase(Locale.getDefault())
        return isRequired == "yes" || isRequired == "true"
    }
    return false
}


/**
 * This method returns [DataViewModel] from the provided context of the [NFormViewDetails]
 */
fun NFormViewDetails.getDataViewModel() =
    ViewModelProvider(getViewContext() as FragmentActivity)[DataViewModel::class.java]

/**
 * This method is used to get the base context for [NFormViewDetails]. Sometimes the context is
 * wrapped inside a [ContextThemeWrapper] but we need its base context which is an [FragmentActivity]
 */
fun NFormViewDetails.getViewContext(): Context {
    val context = view.context
    var activityContext = context
    while (activityContext !is Activity && activityContext is ContextThemeWrapper) {
        activityContext = activityContext.baseContext
    }
    return activityContext
}

/**
 * Crediting @see <a href='https://stackoverflow.com/questions/2586301/set-inputtype-for-an-edittext-programmatically?rq=1'>
 *     StackOverflow Implement InputType on EditText</a>
 */
internal fun getSupportedEditTextTypes() =
    mapOf(
        "date" to (InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_DATE),
        "datetime" to (InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_NORMAL),
        "none" to InputType.TYPE_NULL,
        "number" to (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL),
        "numberDecimal" to (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL),
        "numberPassword" to (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD),
        "numberSigned" to (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED),
        "phone" to InputType.TYPE_CLASS_PHONE,
        "text" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL),
        "textAutoComplete" to InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE,
        "textAutoCorrect" to InputType.TYPE_TEXT_FLAG_AUTO_CORRECT,
        "textCapCharacters" to InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS,
        "textCapSentences" to InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
        "textCapWords" to InputType.TYPE_TEXT_FLAG_CAP_WORDS,
        "textEmailAddress" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS),
        "textEmailSubject" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT),
        "textFilter" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_FILTER),
        "textLongMessage" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE),
        "textMultiLine" to InputType.TYPE_TEXT_FLAG_MULTI_LINE,
        "textNoSuggestions" to InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS,
        "textPassword" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD),
        "textPersonName" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME),
        "textPhonetic" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PHONETIC),
        "textPostalAddress" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS),
        "textShortMessage" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE),
        "textUri" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_URI),
        "textVisiblePassword" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD),
        "textWebEditText" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT),
        "textWebEmailAddress" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS),
        "textWebPassword" to (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD),
        "time" to (InputType.TYPE_CLASS_DATETIME or InputType.TYPE_DATETIME_VARIATION_TIME)
    )


fun FormBuilder.populateResourceMap(clazz: Class<*>) {
    for (field in clazz.declaredFields) {
        resourcesMap[field.name] = try {
            field.getInt(clazz)
        } catch (e: Exception) {
            continue
        }
    }
}

fun NFormView.getOptionMetadata(optionName: String): Map<String, Any>? {
    return viewProperties.options?.first { option ->
        option.name == optionName
    }?.viewMetadata
}

/**
 * ======================================================================================================
 * The following are extension functions for:
 * [String] and [Any]
 * ======================================================================================================
 */
fun String?.splitText(delimiter: String): List<String> {
    return if (this.isNullOrBlank()) {
        ArrayList()
    } else listOf(*this.split(delimiter.toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray())
}

/**
 * This method appends a suffix '*' to the provided [String]
 */
fun String.addRedAsteriskSuffix(): SpannableString {
    if (this.isNotEmpty()) {
        val textWithSuffix = SpannableString("$this *")
        textWithSuffix.setSpan(
            ForegroundColorSpan(Color.RED), textWithSuffix.length - 1, textWithSuffix.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return textWithSuffix
    }
    return SpannableString(this)
}

fun String.getKey(suffix: String) = this.substringBefore(suffix)

fun String.removeAsterisk() = this.trim().removeSuffix("*").trim()

fun Any?.isNotNull() = this != null

fun String.extractKeyValue(): Pair<String, String> {
    val keyDataType = splitText(":")
    return Pair(keyDataType.first().trim(), keyDataType.last().trim())
}

/**
 * Parse Json [String] into a [HashMap] of field key against its [NFormViewData]
 */
fun String.parseFormDataJson(): HashMap<String, NFormViewData> {
    return Gson().parseJson<HashMap<String, NFormViewData>>(this)
        .filter { it.value.value.isNotNull() } as HashMap<String, NFormViewData>
}

fun Any.getJsonFromModel(): String = GsonBuilder()
    .excludeFieldsWithoutExposeAnnotation()
    .create().toJson(this)

fun <T : Enum<T>> Class<T>.convertEnumToSet(): HashSet<String> {
    return EnumSet.allOf(this).map { it.name }.toHashSet()
}