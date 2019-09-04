package com.nerdstone.neatformcore.rules

import android.view.View
import com.nerdstone.neatformcore.domain.view.RulesHandler

class NFormRulesHandler private constructor() : RulesHandler {

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

    override fun setVisibility(view: View, operation: RulesHandler.Operation, condition: Boolean) {
        when {
            operation == RulesHandler.Operation.HIDE && condition ->
                view.visibility = View.GONE
            else -> view.visibility = View.VISIBLE
        }
    }

    override fun setEnabled(view: View, operation: RulesHandler.Operation, condition: Boolean) {
        when {
            operation == RulesHandler.Operation.ENABLE && condition -> {
                view.isEnabled = true
                view.isEnabled = true
            }
            else -> view.isEnabled = false
        }
    }

    override fun filterItems(filterItems: List<String>) {
        //TODO implement functionality for filtering
    }
}
