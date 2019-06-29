package com.nerdstone.neatformcore.rules;

import android.util.Log;
import android.view.View;
import com.nerdstone.neatformcore.domain.model.NFormViewOption;
import com.nerdstone.neatformcore.domain.view.RulesHandler;

public class NFormRulesHandler implements RulesHandler {

  private static String TAG = NFormRulesHandler.class.getCanonicalName();

  public NFormRulesHandler() {
  }

  private void performAction(View view, Operations operations) {
    switch (operations) {
      case HIDE:
        view.setVisibility(View.GONE);
        break;
      case SHOW:
        view.setVisibility(View.VISIBLE);
        break;
      case ENABLE:
        view.setEnabled(true);
        view.setFocusable(true);
        break;
      case DISABLE:
        view.setEnabled(false);
        break;
      default:
        Log.w(TAG, "Unknown Operation detected");
        break;
    }
  }

  @Override
  public void evaluateRule(NFormViewOption viewOption) {

  }

}
