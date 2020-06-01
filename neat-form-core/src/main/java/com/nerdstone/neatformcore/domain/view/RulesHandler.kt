package com.nerdstone.neatformcore.domain.view

import android.view.View
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import java.lang.ref.WeakReference

interface RulesHandler {

    var executableRulesList: HashSet<Rule>

    var formBuilder: WeakReference<FormBuilder>

    fun updateSkipLogicFactAfterEvaluate(evaluationResult: Boolean, rule: Rule?, facts: Facts?)

    fun handleSkipLogic(facts: Facts?)

    fun changeVisibility(value: Boolean?, view: View)

    fun handleCalculations(facts: Facts?)

}
