package com.nerdstone.neatformcore.form.json

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.nerdstone.neatformcore.datasource.AssetFile
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NForm
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.rules.RulesFactory.RulesFileType
import com.nerdstone.neatformcore.utils.SingleRunner
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/***
 * @author Elly Nerdstone
 */
class JsonFormBuilder(override var mainLayout: ViewGroup) : FormBuilder {

    private val viewDispatcher: ViewDispatcher = ViewDispatcher.INSTANCE
    private val rulesFactory: RulesFactory = RulesFactory.INSTANCE
    private val rulesHandler = rulesFactory.rulesHandler
    private val singleRunner = SingleRunner()
    var form: NForm? = null

    init {
        rulesHandler.formBuilder = this
    }

    override fun buildForm(source: String) {
        GlobalScope.launch(Dispatchers.Main) {
            if (form == null) {
                form = parseJsonForm(source)
            }
            launch(Dispatchers.Main) {
                singleRunner.afterPrevious {
                    createFormViews(mainLayout.context)
                }
            }
            launch(Dispatchers.IO) {
                singleRunner.afterPrevious {
                    registerFormRules(mainLayout.context, RulesFileType.YAML)
                }
            }
            launch(Dispatchers.Main) {
                singleRunner.afterPrevious {
                    rulesFactory.refreshHiddenViews()
                }
            }
        }
    }

    private suspend fun parseJsonForm(source: String): NForm? {
        return singleRunner.afterPrevious {
            JsonFormParser.parseJson(
                AssetFile.readAssetFileAsString(
                    mainLayout.context,
                    source
                )
            )
        }
    }

    /***
     * @param context android context
     */
    override fun createFormViews(context: Context) {
        if (form != null) {
            for (formContent in form!!.steps) {
                val rootView = VerticalRootView(context)
                rootView.addChildren(formContent.fields, viewDispatcher)
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
