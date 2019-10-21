package com.nerdstone.neatformcore.rules

import android.content.Context
import com.nerdstone.neatformcore.datasource.AssetFile
import com.nerdstone.neatformcore.domain.model.NFormRule
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.RulesHandler
import com.nerdstone.neatformcore.utils.Constants
import com.nerdstone.neatformcore.utils.Utils
import org.jeasy.rules.api.Facts
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.RuleListener
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.DefaultRulesEngine
import org.jeasy.rules.mvel.MVELRuleFactory
import org.jeasy.rules.support.JsonRuleDefinitionReader
import org.jeasy.rules.support.YamlRuleDefinitionReader
import org.mvel2.CompileException
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class RulesFactory private constructor() : RuleListener {

    private var facts: Facts = Facts()
    private var rulesEngine: DefaultRulesEngine = DefaultRulesEngine()
    private lateinit var currentViewDetails: NFormViewDetails
    private var executableRulesList: HashSet<Rule> = hashSetOf()
    var allRules: Rules? = null
    val rulesHandler: RulesHandler = NFormRulesHandler.INSTANCE
    val subjectsRegistry: HashMap<String, HashSet<NFormRule>> = hashMapOf()

    init {
        rulesEngine.registerRuleListener(this)
    }

    override fun beforeEvaluate(rule: Rule?, facts: Facts?): Boolean = true

    override fun onSuccess(rule: Rule?, facts: Facts?) = Timber.d("%s executed successfully", rule)

    override fun onFailure(rule: Rule?, facts: Facts?, exception: Exception?) =
        when (exception) {
            is CompileException -> Timber.e(exception.cause)
            else -> Timber.e(exception)
        }

    override fun beforeExecute(rule: Rule?, facts: Facts?) = Unit

    override fun afterEvaluate(rule: Rule?, facts: Facts?, evaluationResult: Boolean) =
        rulesHandler.updateSkipLogicFactAfterEvaluate(evaluationResult, rule, facts)

    fun readRulesFromFile(
        context: Context, filePath: String, rulesFileType: RulesFileType
    ) {
        if (allRules == null) {
            val mvelRuleFactory: MVELRuleFactory = when (rulesFileType) {
                RulesFileType.JSON -> MVELRuleFactory(JsonRuleDefinitionReader())
                RulesFileType.YAML -> MVELRuleFactory(YamlRuleDefinitionReader())
            }

            allRules = mvelRuleFactory.createRules(
                BufferedReader(
                    InputStreamReader(AssetFile.openFileAsset(context, filePath))
                )
            )
        }
    }

    private fun updateCurrentViewAndFacts(viewDetails: NFormViewDetails) {
        currentViewDetails = viewDetails
        facts.put(currentViewDetails.name, currentViewDetails.value)
    }

    private fun fireRules() {
        rulesEngine.fire(Rules(executableRulesList), facts)
        rulesHandler.handleSkipLogic(facts)
    }

    fun updateFactsAndExecuteRules(viewDetails: NFormViewDetails) {
        updateCurrentViewAndFacts(viewDetails)
        updateExecutableRules()
        fireRules()
    }

    private fun updateExecutableRules() {
        executableRulesList.clear()
        val dependantViews = subjectsRegistry[currentViewDetails.name]
        dependantViews?.forEach { nFormRule ->
            nFormRule.matchingRules.also {
                if (it.isEmpty()) {
                    nFormRule.matchingRules = getMatchingRules(nFormRule.key)
                }
            }
            executableRulesList.addAll(nFormRule.matchingRules.asIterable())
        }
        rulesHandler.executableRulesList = executableRulesList
    }

    fun registerSubjects(subjects: List<String>, viewProperty: NFormViewProperty) {
        subjects.forEach { subject ->

            val (key, dataType) = Utils.extractKeyValue(subject)
            setDefaultFact(key, dataType)

            if (!subjectsRegistry.containsKey(key)) {
                subjectsRegistry[key] = hashSetOf()
            }
            subjectsRegistry[key]?.add(
                NFormRule(
                    viewProperty.name,
                    hashSetOf()
                )
            )
        }
    }

    fun viewHasVisibilityRule(viewProperty: NFormViewProperty): Boolean {
        val hasVisibilityRule =
            allRules?.map { it.name }?.contains("${viewProperty.name}${Constants.RuleActions.VISIBILITY}")
        return hasVisibilityRule != null && hasVisibilityRule
    }

    private fun setDefaultFact(key: String, dataType: String) {
        when (dataType.toUpperCase(Locale.getDefault())) {
            DataTypes.BOOL.name -> facts.put(key, false)
            DataTypes.NUMBER.name -> facts.put(key, 0)
            DataTypes.TEXT.name -> facts.put(key, null)
            DataTypes.LIST.name -> facts.put(key, listOf<Any>())
            DataTypes.MAP.name -> facts.put(key, linkedMapOf<Any, Any>())
        }
    }

    private fun getMatchingRules(ruleName: String): Set<Rule> {
        if (allRules == null) {
            return hashSetOf()
        }
        return allRules?.filter { it.name.startsWith(ruleName) }!!.toSet()
    }

    enum class DataTypes {
        TEXT, BOOL, NUMBER, LIST, MAP
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
