package com.nerdstone.neatformcore.form.json

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.rules.RulesFactory.RulesFileType
import com.nerdstone.neatformcore.utils.CoroutineContextProvider
import com.nerdstone.neatformcore.utils.SingleRunner
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


/***
 * @author Elly Nerdstone
 */
class JsonFormBuilder(
    override val context: Context, override var fileSource: String, var mainLayout: ViewGroup?
) :
    FormBuilder {

    private val viewDispatcher: ViewDispatcher = ViewDispatcher.INSTANCE
    private val rulesFactory: RulesFactory = RulesFactory.INSTANCE
    private val rulesHandler = rulesFactory.rulesHandler
    private val singleRunner = SingleRunner()
    var coroutineContextProvider: CoroutineContextProvider
    var form: NForm? = null
    override var neatStepperLayout = NeatStepperLayout(context)

    init {
        rulesHandler.formBuilder = this
        coroutineContextProvider = CoroutineContextProvider.Default()
    }

    override fun buildForm(
        jsonFormStepBuilderModel: JsonFormStepBuilderModel?, viewList: List<View>?
    ): FormBuilder {
        GlobalScope.launch(coroutineContextProvider.main) {
            if (form == null) {
                val async = async(coroutineContextProvider.default) {
                    singleRunner.afterPrevious {
                        parseJsonForm(fileSource)
                    }
                }
                form = async.await()
            }
            launch(coroutineContextProvider.io) {
                singleRunner.afterPrevious {
                    registerFormRules(context, RulesFileType.YAML)
                }
            }
            launch(coroutineContextProvider.main) {
                singleRunner.afterPrevious {
                    if (viewList == null)
                        createFormViews(context, arrayListOf(), jsonFormStepBuilderModel)
                    else
                        createFormViews(context, viewList, jsonFormStepBuilderModel)
                }
            }
        }
        return this
    }

    private fun parseJsonForm(source: String): NForm? {
        return JsonFormParser.parseJson(AssetFile.readAssetFileAsString(context, source))
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
                        val rootView = addViewsToVerticalRootView(views, index, formContent)
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
                            (context as AppCompatActivity).supportFragmentManager,
                            fragmentsList
                        )
                    )
                }
                mainLayout != null && jsonFormStepBuilderModel == null -> {
                    val formViews = ScrollView(context)
                    form!!.steps.withIndex().forEach { (index, formContent) ->
                        val rootView = addViewsToVerticalRootView(views, index, formContent)
                        formViews.addView(rootView.initRootView() as View)
                    }
                    mainLayout?.addView(formViews)
                }
                else -> Toast.makeText(
                    context, R.string.form_builder_error, Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun addViewsToVerticalRootView(
        customViews: List<View>?, stepIndex: Int, formContent: NFormContent
    ): VerticalRootView {
        val rootView = VerticalRootView(context)
        val view = customViews?.getOrNull(stepIndex)
        when {
            view != null -> {
                rootView.addView(view)
                rootView.addChildren(formContent.fields, viewDispatcher, true)
            }
            else -> rootView.addChildren(formContent.fields, viewDispatcher)
        }
        return rootView
    }

    override fun registerFormRules(context: Context, rulesFileType: RulesFileType) {
        form?.rulesFile?.also {
            rulesFactory.readRulesFromFile(context, it, rulesFileType)
        }
    }
}

const val FRAGMENT_VIEW = "fragment_view"
const val FRAGMENT_INDEX = "index"

class StepFragment : Step {
    var index: Int? = null
    var formView: View? = null

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
        retainInstance=true
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

    override fun verifyStep(): StepVerificationState {
        TODO("not implemented")
    }

    override fun onSelected() {
        TODO("not implemented")
    }

    override fun onError(stepVerificationState: StepVerificationState) {
        TODO("not implemented")
    }

}
