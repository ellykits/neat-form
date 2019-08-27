package com.nerdstone.neatformcore.domain.view;

import com.nerdstone.neatformcore.domain.model.NFormViewDetails;
import com.nerdstone.neatformcore.rules.RulesFactory;

public interface RulesHandler {

    void evaluateRule(NFormViewDetails viewDetails);

    RulesFactory getRulesFactory();

    enum Operation {
        HIDE, SHOW, DISABLE, ENABLE, FILTER
    }

}
