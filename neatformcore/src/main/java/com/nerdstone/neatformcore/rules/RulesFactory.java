package com.nerdstone.neatformcore.rules;

import android.content.Context;

import com.nerdstone.neatformcore.domain.model.NFormViewOption;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.JsonRuleDefinitionReader;
import org.jeasy.rules.support.YamlRuleDefinitionReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import io.reactivex.Completable;
import timber.log.Timber;

import static com.nerdstone.neatformcore.datasource.AssetFile.openFileAsset;

public class RulesFactory implements RuleListener {

    private static final String TAG = RulesFactory.class.getCanonicalName();
    private static RulesFactory rulesFactory;
    private Rules rules;
    private Facts facts;
    private DefaultRulesEngine rulesEngine;
    private String currentRule;

    private RulesFactory() {
        initRuleFactory();
    }

    public static RulesFactory getInstance() {
        if (rulesFactory == null) {
            rulesFactory = new RulesFactory();
        }
        return rulesFactory;
    }

    public Completable readRulesFromFile(Context context, String filePath,
                                         RulesFileType rulesFileType) {

        return Completable.fromAction(() -> {
            if (rules == null) {
                MVELRuleFactory mvelRuleFactory;
                switch (rulesFileType) {
                    case JSON:
                        mvelRuleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());
                        break;
                    case YAML:
                        mvelRuleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader());
                        break;
                    default:
                        throw new Exception("Unsupported file type. The library only supports YAML and JSON standards at the moment");
                }

                rules = mvelRuleFactory.createRules(new BufferedReader(
                        new InputStreamReader(openFileAsset(context, filePath))));
            }
        });
    }

    private void updateFacts(NFormViewOption viewOption) {
        facts.put(viewOption.getName(), viewOption.getValue());
    }

    private void fireRules() {
        rulesEngine.fire(rules, facts);
        Timber.i("rule fired");
    }

    private void initRuleFactory() {
        facts = new Facts();
        facts.put("calculation", "");
        rulesEngine = new DefaultRulesEngine();
        rulesEngine.registerRuleListener(this);
    }

    @Override
    public boolean beforeEvaluate(Rule rule, Facts facts) {
        Timber.tag(TAG).d("Before evaluation of rule {%s}", rule);
        return getCurrentRule()
                .equalsIgnoreCase(rule.getName());//Only evaluate rule that matches name
    }

    @Override
    public void afterEvaluate(Rule rule, Facts facts, boolean evaluationResult) {
        Timber.tag(TAG).d("After evaluation of rule result: {%s}", evaluationResult);
    }

    @Override
    public void beforeExecute(Rule rule, Facts facts) {
        Timber.tag(TAG).d("Executing rule {%s}", rule);
    }

    @Override
    public void onSuccess(Rule rule, Facts facts) {
        Timber.tag(TAG).d("Rule {%s} fired successfully:", rule);
    }

    @Override
    public void onFailure(Rule rule, Facts facts, Exception exception) {
        Timber.tag(TAG).e(exception);
    }

    private String getCurrentRule() {
        return currentRule;
    }

    private void setCurrentRule(String currentRule) {
        this.currentRule = currentRule;
    }

    public void updateFactsAndExecuteRule(NFormViewOption viewOption) {
        rulesFactory.setCurrentRule(viewOption.getName());
        rulesFactory.updateFacts(viewOption);
        rulesFactory.fireRules();
    }

    public enum RulesFileType {
        JSON, YAML
    }
}
