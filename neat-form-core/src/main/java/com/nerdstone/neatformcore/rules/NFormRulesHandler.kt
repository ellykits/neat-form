package com.nerdstone.neatformcore.rules

import android.app.Activity
import android.view.View
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.view.RulesHandler
import com.nerdstone.neatformcore.utils.Constants
import com.nerdstone.neatformcore.utils.ViewUtils
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import java.util.*

class NFormRulesHandler private constructor() : RulesHandler {

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

    override fun updateSkipLogicFactAfterEvaluate(
        evaluationResult: Boolean, rule: Rule?, facts: Facts?
    ) {
        if (rule != null && facts != null && !evaluationResult) {
            if (facts.asMap().containsKey(rule.name) && rule.name.toLowerCase(Locale.getDefault())
                    .endsWith(Constants.RuleActions.VISIBILITY)
            ) {
                facts.put(rule.name, false)
            }
        }
    }

    override fun handleSkipLogic(facts: Facts?) {
        executableRulesList
            .map { rule -> rule.name }
            .filter {
                it.toLowerCase(Locale.getDefault()).endsWith(Constants.RuleActions.VISIBILITY)
            }
            .map { name ->
                ViewUtils.getKey(name, Constants.RuleActions.VISIBILITY)
            }
            .forEach { key ->
                facts?.let {
                    hideOrShowField(key, it.get<Boolean>("$key${Constants.RuleActions.VISIBILITY}"))
                }
            }
    }

    fun hideOrShowField(key: String, isVisible: Boolean?) {
        if (findViewWithKey(key) != null) {
            changeVisibility(isVisible, findViewWithKey(key)!!)
        }
    }

    private fun findViewWithKey(key: String): View? {
        val activity = formBuilder.context as Activity
        val activityRootView = activity.findViewById<View>(android.R.id.content).rootView
        return activityRootView.findViewWithTag(key)
    }

    override fun refreshViews(allRules: Rules?) {
        val activity = formBuilder.context as Activity
        allRules?.also {
            it.toMutableList()
                .filter { rule ->
                    rule.name.toLowerCase(Locale.getDefault())
                        .endsWith(Constants.RuleActions.VISIBILITY)
                }.forEach { item ->
                    val key = ViewUtils.getKey(item.name, Constants.RuleActions.VISIBILITY)
                    val view = findViewWithKey(key)
                    if (view != null) changeVisibility(false, view)
                }
        }
    }

    override fun changeVisibility(value: Boolean?, view: View) {
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
}
