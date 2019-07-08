package com.nerdstone.neatformcore.views.data;

import com.nerdstone.neatformcore.domain.model.NFormViewOption;
import com.nerdstone.neatformcore.domain.view.DataPassListener;
import com.nerdstone.neatformcore.domain.view.RulesHandler;
import com.nerdstone.neatformcore.rules.NFormRulesHandler;

public class ViewDataHandler implements DataPassListener {

    private static final String TAG = ViewDataHandler.class.getCanonicalName();
    private static ViewDataHandler viewDataHandler;
    private RulesHandler rulesHandler;

    private ViewDataHandler() {
        rulesHandler = NFormRulesHandler.getInstance();
    }

    public static ViewDataHandler getInstance() {
        if (viewDataHandler == null) {
            viewDataHandler = new ViewDataHandler();
        }
        return viewDataHandler;
    }

    @Override
    public void onPassData(NFormViewOption viewOption) {
        rulesHandler.evaluateRule(viewOption);
    }

}
