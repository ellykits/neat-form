package com.nerdstone.neatformcore.rules;

import android.view.View;

import com.nerdstone.neatformcore.domain.view.RulesHandler;

public class NFormRulesHandler implements RulesHandler {

    @Override
    public void performAction(View view, Operations operations) {
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
        }
    }
}
