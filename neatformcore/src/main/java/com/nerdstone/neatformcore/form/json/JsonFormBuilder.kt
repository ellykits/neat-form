package com.nerdstone.neatformcore.form.json

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.nerdstone.neatformcore.datasource.AssetFile
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NForm
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.rules.RulesFactory.RulesFileType
import com.nerdstone.neatformcore.utils.CoroutineContextProvider
import com.nerdstone.neatformcore.utils.SingleRunner
import com.nerdstone.neatformcore.utils.ViewUtils
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
            for ((index, formContent) in form!!.steps.withIndex()) {
                val rootView = VerticalRootView(context)
                val view = views?.getOrNull(index)
                if (view != null) {
                    ViewUtils.updateViews(view, formContent.fields, context, viewDispatcher)
                    rootView.addView(view)
                } else {
                    rootView.addChildren(formContent.fields, viewDispatcher)
                }
                mainLayout.addView(rootView.initRootView() as View)
            }
        }
    }


    override fun registerFormRules(context: Context, rulesFileType: RulesFileType) {
        form?.rulesFile?.also {
            rulesFactory.readRulesFromFile(context, it, rulesFileType)
        }
    }
}
