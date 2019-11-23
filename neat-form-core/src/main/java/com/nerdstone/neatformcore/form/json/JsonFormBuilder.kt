package com.nerdstone.neatformcore.form.json

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.nerdstone.neatandroidstepper.core.model.StepModel
import com.nerdstone.neatandroidstepper.core.stepper.Step
import com.nerdstone.neatandroidstepper.core.stepper.StepVerificationState
import com.nerdstone.neatandroidstepper.core.stepper.StepperPagerAdapter
import com.nerdstone.neatandroidstepper.core.widget.NeatStepperLayout
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.datasource.AssetFile
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NForm
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
class JsonFormBuilder(override var mainLayout: ViewGroup, override var fileSource: String) :
    FormBuilder {

    private val viewDispatcher: ViewDispatcher = ViewDispatcher.INSTANCE
    private val rulesFactory: RulesFactory = RulesFactory.INSTANCE
    private val rulesHandler = rulesFactory.rulesHandler
    private val singleRunner = SingleRunner()
    var coroutineContextProvider: CoroutineContextProvider
    var form: NForm? = null
    var neatStepperLayout : NeatStepperLayout? = null

    init {
        rulesHandler.formBuilder = this
        coroutineContextProvider = CoroutineContextProvider.Default()
    }

    override fun buildForm(viewList: List<View>?): FormBuilder {
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
                    registerFormRules(mainLayout.context, RulesFileType.YAML)
                }
            }
            launch(coroutineContextProvider.main) {
                singleRunner.afterPrevious {
                    if (viewList == null)
                        createFormViews(mainLayout.context, arrayListOf())
                    else
                        createFormViews(mainLayout.context, viewList)
                }
            }
        }
        return this
    }

    private fun parseJsonForm(source: String): NForm? {
        return JsonFormParser.parseJson(
            AssetFile.readAssetFileAsString(
                mainLayout.context,
                source
            )
        )
    }

    /***
     * @param context android context
     */
    override fun createFormViews(context: Context, views: List<View>?) {
        if (form != null) {
            neatStepperLayout = NeatStepperLayout(context)

            val fragmentsList: MutableList<StepFragment> = mutableListOf()

            for ((index, formContent) in form!!.steps.withIndex()) {
                val rootView = VerticalRootView(context)
                val view = views?.getOrNull(index)
                when {
                    view != null -> {
                        rootView.addView(view)
                        rootView.addChildren(formContent.fields, viewDispatcher, true)
                    }
                    else -> rootView.addChildren(formContent.fields, viewDispatcher)
                }
                val stepFragment = StepFragment(
                    StepModel.Builder()
                        .title(formContent.stepName as CharSequence)
                        .bottomNavigationColorResource(R.color.colorBlack)
                        .build(), rootView
                )

                fragmentsList.add(stepFragment)
            }


            neatStepperLayout?.setUpViewWithAdapter(
                StepperPagerAdapter(
                    (context as AppCompatActivity).supportFragmentManager,
                    fragmentsList
                )
            )

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            mainLayout.addView(neatStepperLayout, params)
        }
    }


    override fun registerFormRules(context: Context, rulesFileType: RulesFileType) {
        form?.rulesFile?.also {
            rulesFactory.readRulesFromFile(context, it, rulesFileType)
        }
    }
}


class StepFragment : Step {
    constructor() {
        myView = View(context)
    }

    private var myView: View

    constructor(stepModel: StepModel, v: View) : super(stepModel) {
        myView = v;
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step, container, false)
        val linearLayout = view.findViewById<LinearLayout>(R.id.fragmentLinearLayout)
        linearLayout.addView(myView)
        return view
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
