package com.nerdstone.neatformcore.domain.view;

import com.nerdstone.neatformcore.domain.model.NFormViewOption;

public interface RulesHandler {

    void evaluateRule(NFormViewOption viewOption);

    enum Operations {
        HIDE, SHOW, DISABLE, ENABLE, FILTER
    }

}
