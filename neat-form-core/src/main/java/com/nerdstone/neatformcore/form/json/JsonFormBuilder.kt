package com.nerdstone.neatformcore.form.json

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nerdstone.neatandroidstepper.core.domain.StepperActions
import com.nerdstone.neatandroidstepper.core.model.StepModel
import com.nerdstone.neatandroidstepper.core.stepper.Step
import com.nerdstone.neatandroidstepper.core.stepper.StepVerificationState
import com.nerdstone.neatandroidstepper.core.stepper.StepperPagerAdapter
import com.nerdstone.neatandroidstepper.core.widget.NeatStepperLayout
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.datasource.AssetFile
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.JsonFormStepBuilderModel
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
import com.nerdstone.neatformcore.viewmodel.ReadOnlyFieldsViewModel
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import com.nerdstone.neatformcore.views.containers.RadioGroupView
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.widgets.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
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
class JsonFormBuilder() : FormBuilder, CoroutineScope by MainScope() {
    private var formDataJson: String? = null
    private lateinit var readOnlyFields: MutableSet<String>
    private var mainLayout: ViewGroup? = null
    private val viewDispatcher: ViewDispatcher = ViewDispatcher.INSTANCE
    private val rulesFactory: RulesFactory = RulesFactory.INSTANCE
    private val rulesHandler = rulesFactory.rulesHandler
    private val singleRunner = SingleRunner()
    var defaultContextProvider: DispatcherProvider
    var form: NForm? = null
    var fileSource: String? = null
    override var formString: String? = null
    override lateinit var neatStepperLayout: NeatStepperLayout
    override lateinit var context: Context
    override lateinit var dataViewModel: DataViewModel
    override var formValidator: FormValidator = NeatFormValidator.INSTANCE
    override var registeredViews = hashMapOf<String, KClass<*>>()
    override lateinit var readOnlyFieldsViewModel: ReadOnlyFieldsViewModel

    constructor(context: Context, fileSource: String, mainLayout: ViewGroup?)
            : this() {
        this.context = context
        this.fileSource = fileSource
        this.mainLayout = mainLayout
        this.neatStepperLayout = NeatStepperLayout(context)
        this.dataViewModel =
            ViewModelProvider(context as FragmentActivity)[DataViewModel::class.java]
        this.readOnlyFieldsViewModel =
            ViewModelProvider(context)[ReadOnlyFieldsViewModel::class.java]
    }

    constructor(jsonString: String, context: Context, mainLayout: ViewGroup?)
            : this() {
        this.formString = jsonString
        this.context = context
        this.mainLayout = mainLayout
        this.neatStepperLayout = NeatStepperLayout(context)
        this.dataViewModel =
            ViewModelProvider(context as FragmentActivity)[DataViewModel::class.java]
        this.readOnlyFieldsViewModel =
            ViewModelProvider(context)[ReadOnlyFieldsViewModel::class.java]
    }

    init {
        rulesHandler.formBuilder = this
        formValidator.formBuilder = this
        defaultContextProvider = DefaultDispatcherProvider()
    }

    override fun buildForm(
        jsonFormStepBuilderModel: JsonFormStepBuilderModel?, viewList: List<View>?
    ): FormBuilder {
        registerViews()
        launch(defaultContextProvider.main()) {
            try {
                if (form == null) {
                    form = withContext(defaultContextProvider.default()) {
                        singleRunner.afterPrevious {
                            parseJsonForm()
                        }
                    }
                }

                val rulesAsync = withContext(defaultContextProvider.io()) {
                    singleRunner.afterPrevious {
                        registerFormRulesFromFile(context, RulesFileType.YAML)
                    }
                }
                if (rulesAsync) {
                    withContext(defaultContextProvider.main()) {
                        if (viewList == null)
                            createFormViews(context, arrayListOf(), jsonFormStepBuilderModel)
                        else
                            createFormViews(context, viewList, jsonFormStepBuilderModel)
                    }
                }

            } catch (throwable: Throwable) {
                Timber.e(throwable)
            }
        }
        if (jsonFormStepBuilderModel.isNull()) preFillForm()
        return this
    }

    private fun parseJsonForm(): NForm? {
        return when {
            formString.isNotNull() -> parseJson<NForm>(formString)
            fileSource.isNotNull() -> parseJson<NForm>(
                AssetFile.readAssetFileAsString(context, fileSource!!)
            )
            else -> null
        }
    }

    /***
     *  Create views within the provided [context]. If [jsonFormStepBuilderModel] is also specified use
     *  it to configure the form steps.
     *
     *  If the [views] is also not null, the form will be
     */
    override fun createFormViews(
        context: Context, views: List<View>?, jsonFormStepBuilderModel: JsonFormStepBuilderModel?
    ) {
        if (form.isNotNull()) {
            when {
                jsonFormStepBuilderModel.isNotNull() && (mainLayout == null || mainLayout.isNotNull()) -> {
                    neatStepperLayout.stepperModel = jsonFormStepBuilderModel!!.stepperModel
                    if (jsonFormStepBuilderModel.stepperActions.isNotNull())
                        neatStepperLayout.stepperActions = jsonFormStepBuilderModel.stepperActions
                    val stepFragmentsList = mutableListOf<StepFragment>()
                    form!!.steps.withIndex().forEach { (index, formContent) ->
                        val rootView = VerticalRootView(context)
                        rootView.formBuilder = this
                        addViewsToVerticalRootView(views, index, formContent, rootView)
                        stepFragmentsList.add(
                            StepFragment.newInstance(
                                index, StepModel.Builder().title(form!!.formName)
                                    .subTitle(formContent.stepName as CharSequence)
                                    .build(), rootView, formDataJson
                            )
                        )
                    }
                    neatStepperLayout.setUpViewWithAdapter(
                        StepperPagerAdapter(
                            (context as FragmentActivity).supportFragmentManager, stepFragmentsList
                        )
                    )
                    neatStepperLayout.showLoadingIndicators(false)
                }
                mainLayout.isNotNull() && jsonFormStepBuilderModel == null -> {
                    val formViews = ScrollView(context)
                    form!!.steps.withIndex().forEach { (index, formContent) ->
                        val rootView = VerticalRootView(context)
                        formViews.addView(rootView.initRootView(this) as View)
                        addViewsToVerticalRootView(views, index, formContent, rootView)
                    }
                    mainLayout?.addView(formViews)
                    readOnlyFieldsViewModel.readOnlyFields.value?.let { this.readOnlyFields = it }
                    dataViewModel.details.observe(context as LifecycleOwner, Observer {
                        ViewUtils.updateFieldValues(it, context, readOnlyFields)
                    })
                }
                else -> Toast.makeText(context, R.string.form_builder_error, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun addViewsToVerticalRootView(
        customViews: List<View>?, stepIndex: Int, formContent: NFormContent,
        verticalRootView: VerticalRootView
    ) {
        val view = customViews?.getOrNull(stepIndex)
        when {
            view.isNotNull() -> {
                verticalRootView.addView(view)
                verticalRootView.addChildren(formContent.fields, viewDispatcher, true)
            }
            else -> verticalRootView.addChildren(formContent.fields, viewDispatcher)
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
            return Utils.getJsonFromModel(formDetails)
        }
        FormErrorDialog(context).show()
        return ""
    }

    override fun withFormData(formDataJson: String, readOnlyFields: MutableSet<String>):
            FormBuilder {
        this.formDataJson = formDataJson
        this.readOnlyFields = readOnlyFields
        return this
    }

    override fun preFillForm() {
        if (this::dataViewModel.isInitialized && formDataJson.isNotNull()) {
            this.readOnlyFieldsViewModel.readOnlyFields.value?.addAll(readOnlyFields)
            dataViewModel.updateDetails(FormUtils.parseFormDataJson(formDataJson!!))
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
        return hashMapOf()
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

const val FRAGMENT_VIEW = "fragment_view"
const val FRAGMENT_INDEX = "index"
const val FORM_DATA_JSON = "form_fields_data"

class StepFragment : Step {

    private lateinit var dataViewModel: DataViewModel
    private var index: Int? = null
    private var formView: View? = null
    private var formDataJson: String? = null

    constructor()

    constructor(stepModel: StepModel) : super(stepModel)

    companion object {
        @JvmStatic
        fun newInstance(
            index: Int, stepModel: StepModel, verticalRootView: VerticalRootView,
            formDataJson: String?
        ) =
            StepFragment(stepModel).apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_INDEX, index)
                    putSerializable(FRAGMENT_VIEW, verticalRootView)
                    putString(FORM_DATA_JSON, formDataJson)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is StepperActions) throw ClassCastException("$context MUST implement FormActions")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            index = it.getInt(FRAGMENT_INDEX)
            formView = it.getSerializable(FRAGMENT_VIEW) as VerticalRootView?
            formDataJson = it.getString(FORM_DATA_JSON)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (formView.isNotNull() && formView?.parent.isNotNull()) return formView?.parent as View
        return ScrollView(activity).apply { addView(formView) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(ViewModelProvider(this)[DataViewModel::class.java]) {
            dataViewModel = this
            details.observe(viewLifecycleOwner, Observer {
                ViewUtils.updateFieldValues(it, activity as Context, mutableSetOf())
            })
            formDataJson?.let {
                FormUtils.parseFormDataJson(it).also { data -> dataViewModel.updateDetails(data) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        formView = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //No call for super(). Bug on API Level > 11.
    }

    override fun verifyStep() = StepVerificationState(true, null)

    override fun onSelected() = Unit

    override fun onError(stepVerificationState: StepVerificationState) = Unit
}
