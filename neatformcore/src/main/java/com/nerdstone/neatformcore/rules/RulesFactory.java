package com.nerdstone.neatformcore.rules;

import static com.nerdstone.neatformcore.datasource.AssetFile.openFileAsset;

import android.content.Context;
import com.nerdstone.neatformcore.domain.model.NFormViewOption;
import io.reactivex.Single;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.JsonRuleDefinitionReader;

public class RulesFactory {

  private static RulesFactory rulesFactory;
  private Rules rules;
  private Facts facts;

  private RulesFactory() {
    facts = new Facts();
  }

  public static RulesFactory getInstance() {
    if (rulesFactory == null) {
      rulesFactory = new RulesFactory();
    }
    return rulesFactory;
  }

  public Single<Rules> readRulesFromFile(Context context, String filePath) {
    return Single.fromCallable(() -> {
      if (rules == null) {
        MVELRuleFactory mvelRuleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());
        rules = mvelRuleFactory.createRules(new BufferedReader(
            new InputStreamReader(openFileAsset(context, filePath))));
      }
      return rules;
    });
  }

  public void updateFacts(NFormViewOption viewOption) {
    facts.put(viewOption.getName(), viewOption.getValue());
  }

}
