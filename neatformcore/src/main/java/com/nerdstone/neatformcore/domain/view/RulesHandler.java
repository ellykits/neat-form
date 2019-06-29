package com.nerdstone.neatformcore.domain.view;

import com.nerdstone.neatformcore.domain.model.NFormViewOption;

public interface RulesHandler {

  enum Operations {
    HIDE, SHOW, DISABLE, ENABLE, FILTER
  }

  void evaluateRule(NFormViewOption viewOption);

}
