package com.nerdstone.neatformcore.form.json

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.lifecycle.LifecycleOwner
import com.nerdstone.neatformcore.rules.RulesFactory.RulesFileType
import com.nerdstone.neatformcore.utils.CustomExceptions
import com.nerdstone.neatformcore.utils.SingleRunner
import com.nerdstone.neatformcore.utils.isNotNull
import com.nerdstone.neatformcore.utils.updateFieldValues
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class JsonFormEmbedded(
    private val jsonFormBuilder: JsonFormBuilder, private val mainLayout: ViewGroup
) : CoroutineScope by MainScope() {

    private val singleRunner = SingleRunner()

    fun buildForm(viewList: List<View>? = null): JsonFormEmbedded {
        jsonFormBuilder.registerViews()
        val context = jsonFormBuilder.context
        val defaultContextProvider = jsonFormBuilder.defaultContextProvider
        launch(defaultContextProvider.main() + CustomExceptions.coroutineExceptionHandler) {
            if (jsonFormBuilder.form == null) {
                singleRunner.afterPrevious(defaultContextProvider.default()) {
                    jsonFormBuilder.parseJsonForm()
                }
            }

            val readRules = singleRunner.afterPrevious(defaultContextProvider.io()) {
                jsonFormBuilder.registerFormRulesFromFile(context, RulesFileType.YAML)
            }

            if (readRules) createFormViews(context, viewList ?: arrayListOf())
        }

        jsonFormBuilder.preFillForm()
        return this
    }

    private fun createFormViews(context: Context, views: List<View>?) {
        if (jsonFormBuilder.form.isNotNull() && mainLayout.isNotNull()) {
            val formViews = ScrollView(context)
            jsonFormBuilder.form!!.steps.withIndex().forEach { (index, formContent) ->
                val rootView = VerticalRootView(context).apply { formBuilder = jsonFormBuilder }
                formViews.addView(rootView.initRootView(jsonFormBuilder) as View)
                jsonFormBuilder.addViewsToVerticalRootView(views, index, formContent, rootView)
            }
            mainLayout.addView(formViews)
            jsonFormBuilder.dataViewModel.details.observe(context as LifecycleOwner, {
                context.updateFieldValues(it, jsonFormBuilder.readOnlyFields)
            })
        }
    }
}
