package com.nerdstone.neatformcore.rules

import android.view.View
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.view.RulesHandler
import com.nerdstone.neatformcore.utils.Constants
import com.nerdstone.neatformcore.utils.ViewUtils
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules

class NFormRulesHandler private constructor() : RulesHandler {

    override val viewIdsMap: HashMap<String, Int> = hashMapOf()
    override lateinit var formBuilder: FormBuilder
    override lateinit var executableRulesList: HashSet<Rule>

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

    override fun handleReadOnlyState() {
        TODO("implement toggle enable/disable state on views")
    }

    override fun handleFilter(filterItems: List<String>) {
        TODO("implement functionality for filtering")
    }

    override fun handleSkipLogic(evaluationResult: Boolean, rule: Rule?, facts: Facts?) {
        if (rule != null && facts != null && !evaluationResult) {
            if (facts.asMap().containsKey(rule.name) && rule.name.toLowerCase()
                    .endsWith(Constants.RuleActions.VISIBILITY)
            ) {
                facts.put(rule.name, false)
            }
        }
    }

    override fun hideOrShowViews(facts: Facts?) {
        executableRulesList
            .map { rule -> rule.name }
            .filter { it.toLowerCase().endsWith(Constants.RuleActions.VISIBILITY) }
            .map { name ->
                ViewUtils.getKey(name, Constants.RuleActions.VISIBILITY)
            }
            .forEach { key ->
                facts?.let {
                    val value = facts.get<Boolean>("$key${Constants.RuleActions.VISIBILITY}")
                    val view = formBuilder.mainLayout.findViewById<View>(viewIdsMap[key]!!)
                    changeVisibility(value, view)
                }
            }
    }

    private fun changeVisibility(value: Boolean?, view: View) {
        if (value != null) {
            when {
                value -> {
                    view.animate()
                        .alpha(1.0f)
                        .duration = 800
                    view.visibility = View.VISIBLE
                }
                else -> {
                    view.animate()
                        .alpha(0.0f)
                        .duration = 800
                    view.visibility = View.GONE
                }
            }
        } else {
            view.visibility = View.GONE
        }
    }

    override fun hideViewsInitially(allRules: Rules?) {
        allRules?.also {
            it.toMutableList()
                .filter { rule ->
                    rule.name.toLowerCase().endsWith(Constants.RuleActions.VISIBILITY)
                }.forEach { item ->
                    val key = ViewUtils.getKey(item.name, Constants.RuleActions.VISIBILITY)
                    val view = formBuilder.mainLayout.findViewById<View>(viewIdsMap[key]!!)
                    changeVisibility(false, view)
                }
        }
    }
}
