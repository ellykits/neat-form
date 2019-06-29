package com.nerdstone.neatformcore.rules;

import static com.nerdstone.neatformcore.datasource.AssetFile.openFileAsset;

import android.content.Context;
import io.reactivex.Single;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.JsonRuleDefinitionReader;

public class RulesFactory {

  public static Single<Rules> readRulesFromFile(Context context, String filePath) {
    return Single.fromCallable(() -> {
      MVELRuleFactory mvelRuleFactory = new MVELRuleFactory(new JsonRuleDefinitionReader());
      return mvelRuleFactory.createRules(new BufferedReader(
          new InputStreamReader(openFileAsset(context, filePath))));
    });
  }

}
