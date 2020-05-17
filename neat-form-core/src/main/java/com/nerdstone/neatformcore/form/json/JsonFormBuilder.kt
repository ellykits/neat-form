package com.nerdstone.neatformcore.form.json

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
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
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.form.common.FormErrorDialog
import com.nerdstone.neatformcore.form.json.JsonParser.parseJson
import com.nerdstone.neatformcore.rules.NeatFormValidator
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.rules.RulesFactory.RulesFileType
import com.nerdstone.neatformcore.utils.*
import com.nerdstone.neatformcore.utils.Constants.ViewType
import com.nerdstone.neatformcore.viewmodel.DataViewModel
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
    override lateinit var viewModel: DataViewModel
    override var formValidator: FormValidator = NeatFormValidator.INSTANCE
    override var registeredViews = hashMapOf<String, KClass<*>>()
    private var readOnlyFields = mutableSetOf<String>()

    constructor(context: Context, fileSource: String, mainLayout: ViewGroup?)
            : this() {
        this.context = context
        this.fileSource = fileSource
        this.mainLayout = mainLayout
        this.neatStepperLayout = NeatStepperLayout(context)
        this.viewModel = ViewModelProvider(context as FragmentActivity)[DataViewModel::class.java]
    }

    constructor(jsonString: String, context: Context, mainLayout: ViewGroup?)
            : this() {
        this.formString = jsonString
        this.context = context
        this.mainLayout = mainLayout
        this.neatStepperLayout = NeatStepperLayout(context)
        this.viewModel = ViewModelProvider(context as FragmentActivity)[DataViewModel::class.java]
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
        return this
    }

    private fun parseJsonForm(): NForm? {
        return when {
            formString != null -> parseJson<NForm>(formString)
            fileSource != null -> parseJson<NForm>(
                AssetFile.readAssetFileAsString(context, fileSource!!)
            )
            else -> null
        }
    }

    /***
     * @param context android context
     */
    override fun createFormViews(
        context: Context, views: List<View>?, jsonFormStepBuilderModel: JsonFormStepBuilderModel?
    ) {
        if (form != null) {
            when {
                jsonFormStepBuilderModel != null && (mainLayout == null || mainLayout != null) -> {
                    neatStepperLayout.stepperModel = jsonFormStepBuilderModel.stepperModel

                    if (jsonFormStepBuilderModel.stepperActions != null)
                        neatStepperLayout.stepperActions = jsonFormStepBuilderModel.stepperActions

                    val fragmentsList: MutableList<StepFragment> = mutableListOf()

                    form!!.steps.withIndex().forEach { (index, formContent) ->
                        val rootView = VerticalRootView(context)
                        rootView.formBuilder = this
                        addViewsToVerticalRootView(views, index, formContent, rootView)
                        val stepFragment = StepFragment.newInstance(
                            index,
                            StepModel.Builder()
                                .title(form!!.formName)
                                .subTitle(formContent.stepName as CharSequence)
                                .build(),
                            rootView
                        )
                        fragmentsList.add(stepFragment)
                    }
                    neatStepperLayout.setUpViewWithAdapter(
                        StepperPagerAdapter(
                            (context as FragmentActivity).supportFragmentManager,
                            fragmentsList
                        )
                    )
                    neatStepperLayout.showLoadingIndicators(false)
                }
                mainLayout != null && jsonFormStepBuilderModel == null -> {
                    val formViews = ScrollView(context)
                    form!!.steps.withIndex().forEach { (index, formContent) ->
                        val rootView = VerticalRootView(context)
                        formViews.addView(rootView.initRootView(this) as View)
                        addViewsToVerticalRootView(views, index, formContent, rootView)
                    }
                    mainLayout?.addView(formViews)
                }
                else -> Toast.makeText(
                    context, R.string.form_builder_error, Toast.LENGTH_LONG
                ).show()
            }
        }
        observeViewModel()
    }

    private fun addViewsToVerticalRootView(
        customViews: List<View>?, stepIndex: Int,
        formContent: NFormContent, verticalRootView: VerticalRootView
    ) {

        val view = customViews?.getOrNull(stepIndex)
        when {
            view != null -> {
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
                it[JsonFormConstants.FORM_DATA] = viewModel.details.value
            }

            return Utils.getJsonFromModel(formDetails)
        }
        FormErrorDialog(context).show()
        return ""
    }

    override fun updateFormData(formDataJson: String, readOnlyFields: MutableSet<String>) {
        if (this::viewModel.isInitialized) {
            this.readOnlyFields.addAll(readOnlyFields)
            val viewData = Gson()
                .parseJson<HashMap<String, NFormViewData>>(formDataJson)
                .filter { it.value.value != null }
            viewModel.updateDetails(viewData as HashMap<String, NFormViewData>)
        }
    }

    override fun registerFormRulesFromFile(
        context: Context, rulesFileType: RulesFileType
    ): Boolean {
        form?.rulesFile?.also {
            rulesFactory.readRulesFromFile(context, it, rulesFileType)
        }
        return true
    }

    override fun getFormData(): HashMap<String, NFormViewData> {
        if (formValidator.invalidFields.isEmpty() && formValidator.requiredFields.isEmpty()) {
            return viewModel.details.value!!
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

    /**
     * This method watches for changes in the view model by observing the mutable live data.
     */
    private fun observeViewModel() {
        this.viewModel = ViewModelProvider(context as FragmentActivity)[DataViewModel::class.java]
        viewModel.details.observe(context as FragmentActivity, Observer {
            it.filterNot { entry -> entry.key.endsWith(Constants.RuleActions.CALCULATION) }
                .forEach { entry ->
                    val view: View? = ViewUtils.findViewWithKey(entry.key, context)
                    entry.value.value?.let { realValue ->
                        if (view != null) (view as NFormView).setValue(
                            realValue, !readOnlyFields.contains(entry.key)
                        )
                    }
                    Timber.d("Updated field %s : %s", entry.key, entry.value.value)
                }
        })
    }
}

const val FRAGMENT_VIEW = "fragment_view"
const val FRAGMENT_INDEX = "index"

class StepFragment : Step {
    private var index: Int? = null
    private var formView: View? = null

    constructor()

    constructor(stepModel: StepModel) : super(stepModel)

    companion object {
        fun newInstance(
            index: Int, stepModel: StepModel, verticalRootView: VerticalRootView
        ): StepFragment {

            val args = Bundle().apply {
                putInt(FRAGMENT_INDEX, index)
                putSerializable(FRAGMENT_VIEW, verticalRootView)
            }
            return StepFragment(stepModel).apply { arguments = args }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            index = it.getInt(FRAGMENT_INDEX)
            formView = it.getSerializable(FRAGMENT_VIEW) as VerticalRootView?
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (formView != null && formView?.parent != null) {
            return formView?.parent as View
        }
        val scroller = ScrollView(activity)
        scroller.addView(formView)
        return scroller
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //No call for super(). Bug on API Level > 11.
    }

    override fun verifyStep(): StepVerificationState {
        return StepVerificationState(true, null)
    }

    override fun onSelected() = Unit

    override fun onError(stepVerificationState: StepVerificationState) = Unit

}
