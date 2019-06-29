package com.nerdstone.neatformcore.views.data;

import com.nerdstone.neatformcore.domain.model.NFormViewOption;
import com.nerdstone.neatformcore.domain.view.DataPassListener;
import com.nerdstone.neatformcore.domain.view.RulesHandler;
import com.nerdstone.neatformcore.rules.NFormRulesHandler;
import org.jeasy.rules.api.Facts;

public class ViewDataHandler implements DataPassListener {

  private static final String TAG = ViewDataHandler.class.getCanonicalName();
  private static Facts facts = new Facts();
  private RulesHandler rulesHandler;

  public ViewDataHandler() {
    rulesHandler = new NFormRulesHandler();
  }


  @Override
  public void onPassData(NFormViewOption viewOption) {
    facts.put(viewOption.getName(), viewOption.getValue());
    evaluateRules(viewOption);
  }

  private void evaluateRules(NFormViewOption viewOption) {
    rulesHandler.evaluateRule(viewOption);
  }

}
