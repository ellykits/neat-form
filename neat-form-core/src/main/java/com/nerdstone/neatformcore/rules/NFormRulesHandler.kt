package com.nerdstone.neatformcore.rules

import android.view.View
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.listeners.CalculationChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.view.RulesHandler
import com.nerdstone.neatformcore.utils.Constants
import com.nerdstone.neatformcore.utils.Constants.RuleActions.CALCULATION
import com.nerdstone.neatformcore.utils.DisposableList
import com.nerdstone.neatformcore.utils.ViewUtils
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import java.util.*

class NFormRulesHandler private constructor() : RulesHandler {

    override lateinit var formBuilder: FormBuilder
    override lateinit var executableRulesList: HashSet<Rule>
    var calculationListeners: DisposableList<CalculationChangeListener> = DisposableList()

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

    override fun updateSkipLogicFactAfterEvaluate(
        evaluationResult: Boolean, rule: Rule?, facts: Facts?
    ) {
        if (rule != null && facts != null && !evaluationResult) {
            if (facts.asMap().containsKey(rule.name) && rule.name.toLowerCase(Locale.getDefault())
                    .endsWith(Constants.RuleActions.VISIBILITY)
            ) facts.put(rule.name, false)
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
        filterCurrentRules(CALCULATION)
            .forEach { key ->
                val value = facts?.asMap()?.get(key)
                formBuilder.dataViewModel.saveFieldValue(
                    key.replace(CALCULATION, ""), NFormViewData("Calculation", value, null)
                )
                updateCalculationListeners(Pair(key, value))
            }
    }

    private fun updateCalculationListeners(calculation: Pair<String, Any?>) =
        calculationListeners.get().forEach { it.onCalculationChanged(calculation) }

    fun hideOrShowField(key: String, isVisible: Boolean?) {
        val view = ViewUtils.findViewWithKey(key, formBuilder.context)
        if (view != null) {
            changeVisibility(isVisible, view)
        }
    }

    override fun refreshViews(allRules: Rules?) {
        allRules?.also {
            it.toMutableList()
                .filter { rule ->
                    rule.name.toLowerCase(Locale.getDefault())
                        .endsWith(Constants.RuleActions.VISIBILITY)
                }.forEach { item ->
                    val key = ViewUtils.getKey(item.name, Constants.RuleActions.VISIBILITY)
                    val view = ViewUtils.findViewWithKey(key, formBuilder.context)
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
