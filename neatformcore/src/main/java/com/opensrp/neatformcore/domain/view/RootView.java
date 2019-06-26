package com.opensrp.neatformcore.domain.view;

import com.opensrp.neatformcore.domain.model.NFormViewData;
import com.opensrp.neatformcore.domain.model.NFormViewProperty;
import com.opensrp.neatformcore.views.data.ViewDataHandler;

import java.util.List;

public interface RootView {
    RootView initRootView();

    void addChild(NFormView nFormView);

    void addChildren(List<NFormViewProperty> viewProperties, ViewDataHandler viewDataHandler);

    List<NFormViewData> getViewsData();

}
