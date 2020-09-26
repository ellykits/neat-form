package com.nerdstone.neatformcore.form.json

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.nerdstone.neatformcore.datasource.AssetFile
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NForm
import com.nerdstone.neatformcore.domain.model.NFormContent
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.form.common.FormErrorDialog
import com.nerdstone.neatformcore.form.json.JsonParser.parseJson
import com.nerdstone.neatformcore.rules.NeatFormValidator
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.rules.RulesFactory.RulesFileType
import com.nerdstone.neatformcore.utils.*
import com.nerdstone.neatformcore.utils.Constants.ViewType
import com.nerdstone.neatformcore.viewmodel.DataViewModel
import com.nerdstone.neatformcore.viewmodel.FormViewModel
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import com.nerdstone.neatformcore.views.containers.RadioGroupView
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.widgets.*
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

object JsonFormConstants {
    const val FORM_VERSION = "form_version"
    const val FORM_METADATA = "form_metadata"
    const val FORM_DATA = "form_data"
    const val FORM_NAME = "form_name"
}

/***
 * @author Elly Nerdstone
 *
 * This class implements the [FormBuilder] and is used to build the form using JSON specification.
 * This form builder works in 3 sequential steps. First it parses the JSON into a model, secondly it
 * reads the rules file and finally creates the actual views. All these three tasks run one after the
 * other but within a [CoroutineScope].
 *
 * To ensure that the the form builder will not attempt to create the views without first parsing
 * the JSON a mutex [SingleRunner] is used to guarantee that the tasks run sequentially. This
 * does not mean however that the tasks are running on the main thread.
 *
 * To parse the JSON for example is handled by [DispatcherProvider.default] method since it is a CPU intensive task.
 * Reading of rules file however is run by [DispatcherProvider.io] whereas creation of views is done on the
 * main thread using [DispatcherProvider.main] scope.
 */
class JsonFormBuilder() : FormBuilder {
    internal val readOnlyFields = mutableSetOf<String>()
    override lateinit var context: Context
    override lateinit var dataViewModel: DataViewModel
    override lateinit var formViewModel: FormViewModel
    override val rulesFactory: RulesFactory = RulesFactory()
    override val viewDispatcher: ViewDispatcher = ViewDispatcher(rulesFactory)
    override val formValidator: FormValidator = NeatFormValidator()
    override var registeredViews = mutableMapOf<String, KClass<*>>()
    override var resourcesMap = mutableMapOf<String, Int>()
    private val rulesHandler = rulesFactory.rulesHandler
    var defaultContextProvider: DispatcherProvider
    var form: NForm? = null
    var fileSource: String? = null
    var formDataJson: String? = null
    override var formString: String? = null

    constructor(context: Context, fileSource: String) : this() {
        this.context = context
        this.fileSource = fileSource
        this.dataViewModel =
            ViewModelProvider(context as FragmentActivity)[DataViewModel::class.java]
        this.formViewModel = ViewModelProvider(context)[FormViewModel::class.java]

    }

    constructor(jsonString: String, context: Context) : this() {
        this.formString = jsonString
        this.context = context
        this.dataViewModel =
            ViewModelProvider(context as FragmentActivity)[DataViewModel::class.java]
        this.formViewModel = ViewModelProvider(context)[FormViewModel::class.java]
    }

    init {
        rulesHandler.formBuilder = WeakReference(this)
        defaultContextProvider = DefaultDispatcherProvider()
    }

    internal fun parseJsonForm(): NForm? {
        return when {
            formString.isNotNull() -> parseJson<NForm>(formString)
            fileSource.isNotNull() -> parseJson<NForm>(
                AssetFile.readAssetFileAsString(context, fileSource!!)
            )
            else -> null
        }
    }

    internal fun addViewsToVerticalRootView(
        customViews: List<View>?, stepIndex: Int, formContent: NFormContent,
        verticalRootView: VerticalRootView
    ) {
        val view = customViews?.getOrNull(stepIndex)
        when {
            view.isNotNull() -> {
                verticalRootView.addView(view)
                verticalRootView.addChildren(formContent.fields, this, true)
            }
            else -> verticalRootView.addChildren(formContent.fields, this)
        }
    }

    override fun getFormMetaData(): Map<String, Any> {
        return form?.formMetadata ?: mutableMapOf()
    }

    override fun getFormDataAsJson(): String {
        if (formValidator.invalidFields.isEmpty() && formValidator.requiredFields.isEmpty()) {
            val formDetails = hashMapOf<String, Any?>().also {
                it[JsonFormConstants.FORM_NAME] = form?.formName
                it[JsonFormConstants.FORM_METADATA] = form?.formMetadata
                it[JsonFormConstants.FORM_VERSION] = form?.formVersion
                it[JsonFormConstants.FORM_DATA] = dataViewModel.details.value
            }
            return formDetails.getJsonFromModel()
        }
        FormErrorDialog(context).show()
        logInvalidFields()
        return ""
    }

    override fun withFormData(formDataJson: String, readOnlyFields: MutableSet<String>):
            FormBuilder {
        this.formDataJson = formDataJson
        this.readOnlyFields.addAll(readOnlyFields)
        return this
    }

    override fun preFillForm() {
        if (
            this::dataViewModel.isInitialized && dataViewModel.details.value.isNullOrEmpty()
            && formDataJson.isNotNull()
        ) {
            this.formViewModel.readOnlyFields.value?.addAll(readOnlyFields)
            dataViewModel.updateDetails(formDataJson!!.parseFormDataJson())
        }
    }

    override fun registerFormRulesFromFile(context: Context, rulesFileType: RulesFileType)
            : Boolean {
        form?.rulesFile?.also {
            rulesFactory.readRulesFromFile(context, it, rulesFileType)
        }
        return true
    }

    override fun getFormData(): HashMap<String, NFormViewData> {
        if (formValidator.invalidFields.isEmpty() && formValidator.requiredFields.isEmpty()) {
            return dataViewModel.details.value!!
        }
        FormErrorDialog(context).show()
        logInvalidFields()
        return hashMapOf()
    }

    private fun logInvalidFields() {
        Timber.d("Invalid fields (${formValidator.invalidFields.size}) -> ${formValidator.invalidFields}")
        Timber.d("Required fields (${formValidator.requiredFields.size}) -> ${formValidator.requiredFields}")
    }

    override fun registerViews() {
        registeredViews[ViewType.EDIT_TEXT] = EditTextNFormView::class
        registeredViews[ViewType.TEXT_INPUT_EDIT_TEXT] = TextInputEditTextNFormView::class
        registeredViews[ViewType.NUMBER_SELECTOR] = NumberSelectorNFormView::class
        registeredViews[ViewType.SPINNER] = SpinnerNFormView::class
        registeredViews[ViewType.DATETIME_PICKER] = DateTimePickerNFormView::class
        registeredViews[ViewType.CHECKBOX] = CheckBoxNFormView::class
        registeredViews[ViewType.MULTI_CHOICE_CHECKBOX] = MultiChoiceCheckBox::class
        registeredViews[ViewType.RADIO_GROUP] = RadioGroupView::class
        registeredViews[ViewType.TOAST_NOTIFICATION] = NotificationNFormView::class
    }
}