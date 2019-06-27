package com.nerdstone.neatformcore.domain.view;

import android.view.View;

public interface RulesHandler {

    enum Operations {
        HIDE, SHOW, DISABLE, ENABLE, FILTER
    }

    void performAction(View view, Operations operations);
}
