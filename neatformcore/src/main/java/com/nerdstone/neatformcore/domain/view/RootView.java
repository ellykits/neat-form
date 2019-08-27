package com.nerdstone.neatformcore.domain.view;

import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty;
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher;

import java.util.List;

public interface RootView {

    RootView initRootView();

    void addChild(NFormView nFormView);

    void addChildren(List<NFormViewProperty> viewProperties, ViewDispatcher viewDispatcher);

    List<NFormViewData> getViewsData();

    void setupView();

}
