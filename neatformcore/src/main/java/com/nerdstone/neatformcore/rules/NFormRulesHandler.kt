package com.nerdstone.neatformcore.rules

import android.view.View

import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.view.RulesHandler

class NFormRulesHandler private constructor() : RulesHandler {

    override fun evaluateRule(viewDetails: NFormViewDetails) {
        rulesFactory.updateFactsAndExecuteRule(viewDetails)
    }

    override val rulesFactory: RulesFactory = RulesFactory.INSTANCE


    companion object {

        @Volatile
        private var instance: NFormRulesHandler? = null

        val INSTANCE: NFormRulesHandler
            get() = instance ?: synchronized(this) {
                NFormRulesHandler().also {
                    instance = it
                }
            }
    }

    fun performAction(view: View, operation: RulesHandler.Operation, condition: Boolean) {
        if (operation == RulesHandler.Operation.HIDE && condition) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }

        if (operation == RulesHandler.Operation.ENABLE && condition) {
            view.isEnabled = true
            view.isFocusable = true
        } else {
            view.isEnabled = false
        }
    }
}
