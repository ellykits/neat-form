package com.nerdstone.neatformcore.domain.view;

import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.NFormViewProperty;
import com.nerdstone.neatformcore.views.data.ViewDataHandler;
import java.util.List;

public interface RootView {

  RootView initRootView();

  void addChild(NFormView nFormView);

  void addChildren(List<NFormViewProperty> viewProperties, ViewDataHandler viewDataHandler);

  List<NFormViewData> getViewsData();

  void setupView();

}
