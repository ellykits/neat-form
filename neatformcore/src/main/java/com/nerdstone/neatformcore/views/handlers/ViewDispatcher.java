package com.nerdstone.neatformcore.views.handlers;

import com.nerdstone.neatformcore.domain.model.NFormViewDetails;
import com.nerdstone.neatformcore.domain.view.DataActionListener;
import com.nerdstone.neatformcore.domain.view.RulesHandler;
import com.nerdstone.neatformcore.rules.NFormRulesHandler;

public class ViewDispatcher implements DataActionListener {

    private static final String TAG = ViewDispatcher.class.getCanonicalName();
    private static ViewDispatcher viewDispatcher;
    private RulesHandler rulesHandler;

    private ViewDispatcher() {
        rulesHandler = NFormRulesHandler.getInstance();
    }

    public static ViewDispatcher getInstance() {
        if (viewDispatcher == null) {
            viewDispatcher = new ViewDispatcher();
        }
        return viewDispatcher;
    }

    @Override
    public void onPassData(NFormViewDetails viewDetails) {
        rulesHandler.evaluateRule(viewDetails);
    }

    public RulesHandler getRulesHandler() {
        return rulesHandler;
    }
}
