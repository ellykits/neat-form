package com.nerdstone.neatformcore.views.handlers

import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.rules.RulesFactory

class ViewDispatcher private constructor() : DataActionListener {

    val rulesFactory: RulesFactory = RulesFactory.INSTANCE

    override fun onPassData(viewDetails: NFormViewDetails) {
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
