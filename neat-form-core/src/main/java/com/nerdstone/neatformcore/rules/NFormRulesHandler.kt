package com.nerdstone.neatformcore.rules

import android.app.Activity
import android.view.View
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.listeners.CalculationChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewData
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
    var calculationChangeListener: CalculationChangeListener? = null

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
        val suffix = Constants.RuleActions.VISIBILITY
        filterCurrentRules(suffix)
            .map { name ->
                ViewUtils.getKey(name, suffix)
            }
            .forEach { key ->
                facts?.also {
                    hideOrShowField(key, it.get<Boolean>("$key${Constants.RuleActions.VISIBILITY}"))
                }
            }
    }

    private fun filterCurrentRules(suffix: String): List<String> {
        return executableRulesList
            .map { rule -> rule.name }
            .filter {
                it.toLowerCase(Locale.getDefault()).endsWith(suffix)
            }
    }

    override fun handleCalculations(facts: Facts?) {
        filterCurrentRules(Constants.RuleActions.CALCULATION)
            .forEach { key ->
                val value = facts?.asMap()?.get(key)
                formBuilder.viewModel.details[key] =
                    NFormViewData(
                        type = "Calculation", value = value, metadata = null
                    )
                calculationChangeListener?.onCalculationChanged(Pair(key, value))
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

    override fun changeVisibility(value: Boolean?, view: View) = if (value != null)
        when {
            value -> view.visibility = View.VISIBLE
            else -> view.visibility = View.GONE
        } else {
        view.visibility = View.GONE
    }
}
