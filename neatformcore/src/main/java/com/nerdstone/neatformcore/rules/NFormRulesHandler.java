package com.nerdstone.neatformcore.rules;

import android.view.View;

import com.nerdstone.neatformcore.domain.model.NFormViewOption;
import com.nerdstone.neatformcore.domain.view.RulesHandler;

import timber.log.Timber;

public class NFormRulesHandler implements RulesHandler {

    private static String TAG = NFormRulesHandler.class.getCanonicalName();
    private static NFormRulesHandler rulesHandler;
    private RulesFactory rulesFactory;

    private NFormRulesHandler() {
        rulesFactory = RulesFactory.getInstance();
    }

    public static NFormRulesHandler getInstance() {
        if (rulesHandler == null) {
            rulesHandler = new NFormRulesHandler();
        }
        return rulesHandler;
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
                Timber.tag(TAG).w("Unknown Operation detected");
                break;
        }
    }

    @Override
    public void evaluateRule(NFormViewOption viewOption) {
        if (viewOption != null) {
            rulesFactory.updateFactsAndExecuteRule(viewOption);
        }
    }


}
