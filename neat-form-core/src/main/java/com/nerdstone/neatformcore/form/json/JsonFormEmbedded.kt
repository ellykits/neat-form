package com.nerdstone.neatformcore.form.json

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.lifecycle.LifecycleOwner
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.utils.SingleRunner
import com.nerdstone.neatformcore.utils.isNotNull
import com.nerdstone.neatformcore.utils.updateFieldValues
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class JsonFormEmbedded(
    private val jsonFormBuilder: JsonFormBuilder, private val mainLayout: ViewGroup
) : CoroutineScope by MainScope() {

    private val singleRunner = SingleRunner()

    fun buildForm(viewList: List<View>? = null): JsonFormEmbedded {
        jsonFormBuilder.registerViews()
        val context = jsonFormBuilder.context
        launch(jsonFormBuilder.defaultContextProvider.main()) {
            try {
                if (jsonFormBuilder.form == null) {
                    jsonFormBuilder.form =
                        withContext(jsonFormBuilder.defaultContextProvider.default()) {
                            singleRunner.afterPrevious {
                                jsonFormBuilder.parseJsonForm()
                            }
                        }
                }

                val rulesAsync = withContext(jsonFormBuilder.defaultContextProvider.io()) {
                    singleRunner.afterPrevious {
                        jsonFormBuilder.registerFormRulesFromFile(
                            context,
                            RulesFactory.RulesFileType.YAML
                        )
                    }
                }
                if (rulesAsync) {
                    withContext(jsonFormBuilder.defaultContextProvider.main()) {
                        createFormViews(context, viewList ?: arrayListOf())
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
        if (jsonFormBuilder.form.isNotNull() && mainLayout.isNotNull()) {
            val formViews = ScrollView(context)
            jsonFormBuilder.form!!.steps.withIndex().forEach { (index, formContent) ->
                val rootView =
                    VerticalRootView(context).apply { formBuilder = jsonFormBuilder }
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
