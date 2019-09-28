package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.builders.FormBuilder
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules

interface RulesHandler {

    var executableRulesList: HashSet<Rule>

    var formBuilder: FormBuilder

    val viewIdsMap: HashMap<String, Int>

    fun handleReadOnlyState()

    fun handleFilter(filterItems: List<String>)

    fun handleSkipLogic(evaluationResult: Boolean, rule: Rule?, facts: Facts?)

    fun hideOrShowViews(facts: Facts?)

    fun hideViewsInitially(allRules: Rules?)

}
