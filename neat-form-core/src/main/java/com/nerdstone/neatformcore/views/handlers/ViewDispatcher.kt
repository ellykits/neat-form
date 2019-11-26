package com.nerdstone.neatformcore.views.handlers

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.viewmodel.DataViewModel

class ViewDispatcher private constructor() : DataActionListener {

    val rulesFactory: RulesFactory = RulesFactory.INSTANCE

    override fun onPassData(viewDetails: NFormViewDetails) {

        //Save the passed data to view model
        val viewModel =
            ViewModelProviders.of(viewDetails.view.context as FragmentActivity)[DataViewModel::class.java]
        viewModel.details[viewDetails.name] = viewDetails.value

        //Only execute rule if view has dependants
        if (rulesFactory.subjectsRegistry.containsKey(viewDetails.name.trim())) {
            rulesFactory.updateFactsAndExecuteRules(viewDetails)
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
