package com.nerdstone.neatformcore.views.handlers

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.viewmodel.DataViewModel

class ViewDispatcher private constructor() : DataActionListener {

    val rulesFactory: RulesFactory = RulesFactory.INSTANCE

    /**
     * Validates view data if it passes proceed to save the view data and fire rules
     * Otherwise do nothing at all
     * @param viewDetails the details of the view that has just dispatched a value
     */
    override fun onPassData(viewDetails: NFormViewDetails) {

        val viewModel =
            ViewModelProviders.of(viewDetails.view.context as FragmentActivity)[DataViewModel::class.java]

        if ((viewDetails.view as NFormView).validateValue()) {
            viewModel.details[viewDetails.name] =
                NFormViewData(
                    viewDetails.view.javaClass.simpleName,
                    viewDetails.value,
                    viewDetails.metadata
                )

            if (rulesFactory.subjectsRegistry.containsKey(viewDetails.name.trim())) {
                rulesFactory.updateFactsAndExecuteRules(viewDetails)
            }
        } else {
            viewModel.details.remove(viewDetails.name)
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
