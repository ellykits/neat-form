package com.nerdstone.neatformcore.rules

import android.content.Context
import com.nerdstone.neatformcore.datasource.AssetFile.Companion.openFileAsset
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import io.reactivex.Completable
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.RuleListener
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.DefaultRulesEngine
import org.jeasy.rules.mvel.MVELRuleFactory
import org.jeasy.rules.support.JsonRuleDefinitionReader
import org.jeasy.rules.support.YamlRuleDefinitionReader
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class RulesFactory private constructor() : RuleListener {
    private var rules: Rules? = null
    private var facts: Facts? = null
    private var rulesEngine: DefaultRulesEngine? = null
    private var currentRule: String? = null
    var subjectsRegistry: Map<String, Set<String>>? = null
        private set

    init {
        initRuleFactory()
    }

    fun readRulesFromFile(
        context: Context,
        filePath: String,
        rulesFileType: RulesFileType
    ): Completable {
        return Completable.fromAction {
            if (rules == null) {
                val mvelRuleFactory: MVELRuleFactory = when (rulesFileType) {
                    RulesFileType.JSON -> MVELRuleFactory(JsonRuleDefinitionReader())
                    RulesFileType.YAML -> MVELRuleFactory(YamlRuleDefinitionReader())
                }

                rules = mvelRuleFactory.createRules(
                    BufferedReader(
                        InputStreamReader(openFileAsset(context, filePath))
                    )
                )
            }
        }
    }

    private fun updateFacts(viewDetails: NFormViewDetails) {
        facts!!.put(viewDetails.name!!, viewDetails.value)
    }

    private fun fireRules() {
        rulesEngine?.fire(rules!!, facts)
        Timber.i("rule fired")
    }

    private fun initRuleFactory() {
        facts = Facts()
        rulesEngine = DefaultRulesEngine()
        rulesEngine?.registerRuleListener(this)
        subjectsRegistry = HashMap()
    }

    override fun beforeEvaluate(rule: Rule, facts: Facts): Boolean {
        Timber.d("Before evaluation of rule {%s}", rule)
        return rule.name.startsWith(currentRule!!)
    }

    override fun afterEvaluate(rule: Rule, facts: Facts, evaluationResult: Boolean) {
        if (evaluationResult) {
            Timber.d("Cool {%s} evaluated as 'true'", rule.name)
        }
    }

    override fun beforeExecute(rule: Rule, facts: Facts) {
        Timber.d("Executing rule {%s}", rule)
    }

    override fun onSuccess(rule: Rule, facts: Facts) {
        Timber.d("Rule {%s} fired successfully:", rule)
    }

    override fun onFailure(rule: Rule, facts: Facts, exception: Exception) {
        Timber.e(exception)
    }

    fun updateFactsAndExecuteRule(viewDetails: NFormViewDetails) {
        currentRule = viewDetails.name
        updateFacts(viewDetails)
        fireRules()
    }

    enum class RulesFileType {
        JSON, YAML
    }

    companion object {
        @Volatile
        private var instance: RulesFactory? = null

        val INSTANCE: RulesFactory
            get() = instance ?: synchronized(this) {
                RulesFactory().also {
                    instance = it
                }
            }
    }
}
