package com.nerdstone.neatformcore.rules;

import android.view.View;

import com.nerdstone.neatformcore.domain.model.NFormViewDetails;
import com.nerdstone.neatformcore.domain.view.RulesHandler;

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

    public static void performAction(View view, Operation operation, boolean condition) {
        if (operation == Operation.HIDE && condition) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }

        if (operation == Operation.ENABLE && condition) {
            view.setEnabled(true);
            view.setFocusable(true);
        } else {
            view.setEnabled(false);
        }
    }

    @Override
    public void evaluateRule(NFormViewDetails viewDetails) {
        if (viewDetails != null) {
            rulesFactory.updateFactsAndExecuteRule(viewDetails);
        }
    }

    @Override
    public RulesFactory getRulesFactory() {
        return rulesFactory;
    }


}
