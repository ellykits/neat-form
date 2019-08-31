package com.nerdstone.neatformcore.views.handlers

import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.view.DataActionListener
import com.nerdstone.neatformcore.domain.view.RulesHandler
import com.nerdstone.neatformcore.rules.NFormRulesHandler

class ViewDispatcher private constructor() : DataActionListener {
    val rulesHandler: RulesHandler

    init {
        rulesHandler = NFormRulesHandler.INSTANCE
    }

    override fun onPassData(viewDetails: NFormViewDetails) {
        rulesHandler.evaluateRule(viewDetails)
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
