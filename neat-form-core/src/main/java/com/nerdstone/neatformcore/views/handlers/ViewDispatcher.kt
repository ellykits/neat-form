package com.nerdstone.neatformcore.views.handlers

import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.nerdstone.neatformcore.domain.listeners.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.viewmodel.DataViewModel

class ViewDispatcher private constructor() : DataActionListener {

    val rulesFactory: RulesFactory = RulesFactory.INSTANCE

    /**
     * Dispatches an action when view value changes. If value is the same as what had already been
     * dispatched then do nothing
     * @param viewDetails the details of the view that has just dispatched a value
     */
    override fun onPassData(viewDetails: NFormViewDetails) {
        val context = viewDetails.view.context
        var activityContext = context
        if (context is ContextThemeWrapper) {
            activityContext = context.baseContext
        }
        val viewModel = ViewModelProvider(activityContext as FragmentActivity)[DataViewModel::class.java]

        if (viewModel.details[viewDetails.name] != viewDetails.value) {
            viewModel.details[viewDetails.name] =
                    NFormViewData(viewDetails.view.javaClass.simpleName, viewDetails.value, viewDetails.metadata)

            val nFormView = viewDetails.view as NFormView
            nFormView.validateValue()

            //Fire rules for calculations and other fields watching on this current field
            val calculations = (viewDetails.view as NFormView).viewProperties.calculations
            if (rulesFactory.subjectsRegistry.containsKey(viewDetails.name.trim()) || calculations != null) {
                rulesFactory.updateFactsAndExecuteRules(viewDetails)
            }

            if (viewDetails.value == null && Utils.isFieldRequired(nFormView)
                    && viewDetails.view.visibility == View.VISIBLE
            ) {
                nFormView.formValidator.requiredFields.add(viewDetails.name)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: ViewDispatcher? = null

        val INSTANCE: ViewDispatcher
            get() = instance ?: synchronized(this) {
                ViewDispatcher().also {
                    instance = it
                }
            }
    }
}
