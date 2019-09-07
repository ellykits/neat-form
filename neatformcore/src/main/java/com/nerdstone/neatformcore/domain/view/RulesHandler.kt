package com.nerdstone.neatformcore.domain.view

import android.view.View

interface RulesHandler {

    enum class Operation {
        HIDE, SHOW, DISABLE, ENABLE, FILTER
    }

    fun setVisibility(view: View, operation: Operation, condition: Boolean)

    fun setEnabled(view: View, operation: Operation, condition: Boolean)

    fun filterItems(filterItems: List<String>)

}
