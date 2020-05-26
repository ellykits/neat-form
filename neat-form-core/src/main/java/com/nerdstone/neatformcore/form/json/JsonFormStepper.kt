package com.nerdstone.neatformcore.form.json

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.nerdstone.neatandroidstepper.core.model.StepModel
import com.nerdstone.neatandroidstepper.core.stepper.Step
import com.nerdstone.neatandroidstepper.core.stepper.StepVerificationState
import com.nerdstone.neatandroidstepper.core.stepper.StepperPagerAdapter
import com.nerdstone.neatandroidstepper.core.widget.NeatStepperLayout
import com.nerdstone.neatformcore.form.common.FormActions
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.utils.SingleRunner
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.isNotNull
import com.nerdstone.neatformcore.viewmodel.DataViewModel
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class JsonFormStepper(
    private val jsonFormBuilder: JsonFormBuilder, private val neatStepperLayout: NeatStepperLayout
) : CoroutineScope by MainScope() {

    private val singleRunner = SingleRunner()

    fun buildForm(viewList: List<View>? = null): JsonFormStepper {
        jsonFormBuilder.registerViews()
        val defaultContextProvider = jsonFormBuilder.defaultContextProvider
        launch(defaultContextProvider.main()) {
            try {
                if (jsonFormBuilder.form == null) {
                    jsonFormBuilder.form = withContext(defaultContextProvider.default()) {
                        singleRunner.afterPrevious {
                            jsonFormBuilder.parseJsonForm()
                        }
                    }
                }

                val rulesAsync = withContext(defaultContextProvider.io()) {
                    singleRunner.afterPrevious {
                        jsonFormBuilder.registerFormRulesFromFile(
                            jsonFormBuilder.context,
                            RulesFactory.RulesFileType.YAML
                        )
                    }
                }
                if (rulesAsync) {
                    withContext(defaultContextProvider.main()) {
                        createFormViews(jsonFormBuilder.context, viewList ?: arrayListOf())
                    }
                }

            } catch (throwable: Throwable) {
                Timber.e(throwable)
            }
        }
        jsonFormBuilder.preFillForm()
        return this
    }

    private fun createFormViews(context: Context, views: List<View>?) {
        if (jsonFormBuilder.form.isNotNull()) {
            val stepFragmentsList = mutableListOf<StepFragment>()
            jsonFormBuilder.form!!.steps.withIndex().forEach { (index, formContent) ->
                val rootView = VerticalRootView(context)
                rootView.formBuilder = jsonFormBuilder
                jsonFormBuilder.addViewsToVerticalRootView(
                    views, index, formContent, rootView
                )
                stepFragmentsList.add(
                    StepFragment.newInstance(
                        index, StepModel.Builder().title(jsonFormBuilder.form!!.formName)
                            .subTitle(formContent.stepName as CharSequence)
                            .build(), rootView
                    )
                )
            }
            neatStepperLayout.apply {
                showLoadingIndicators(false)
                setUpViewWithAdapter(
                    StepperPagerAdapter(
                        (context as FragmentActivity).supportFragmentManager, stepFragmentsList
                    )
                )
            }
        }
    }
}

const val FRAGMENT_VIEW = "fragment_view"
const val FRAGMENT_INDEX = "index"

class StepFragment : Step {

    private lateinit var dataViewModel: DataViewModel
    private var index: Int? = null
    private var formView: View? = null

    constructor()

    constructor(stepModel: StepModel) : super(stepModel)

    companion object {
        @JvmStatic
        fun newInstance(
            index: Int, stepModel: StepModel, verticalRootView: VerticalRootView
        ) =
            StepFragment(stepModel).apply {
                arguments = Bundle().apply {
                    putInt(FRAGMENT_INDEX, index)
                    putSerializable(FRAGMENT_VIEW, verticalRootView)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is FormActions) throw ClassCastException("$context MUST implement StepperActions")
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
        if (formView.isNotNull() && formView?.parent.isNotNull()) {
            return formView?.parent as View
        }
        return ScrollView(activity).apply { addView(formView) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataViewModel = (activity as FormActions).formBuilder.dataViewModel
        with(dataViewModel) {
            details.observe(viewLifecycleOwner, Observer {
                ViewUtils.updateFieldValues(it, activity as Context, mutableSetOf())
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //No call for super(). Bug on API Level > 11.
    }

    override fun verifyStep() = StepVerificationState(true, null)

    override fun onSelected() = Unit

    override fun onError(stepVerificationState: StepVerificationState) = Unit
}
