package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.rules.RulesFactory

interface RulesHandler {

    val rulesFactory: RulesFactory

    fun evaluateRule(viewDetails: NFormViewDetails)

    enum class Operation {
        HIDE, SHOW, DISABLE, ENABLE, FILTER
    }

}
