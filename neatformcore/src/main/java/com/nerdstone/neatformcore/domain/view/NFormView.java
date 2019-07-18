package com.nerdstone.neatformcore.domain.view;

import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.NFormViewOption;
import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty;
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher;

public interface NFormView {

    NFormViewOption getViewOption();

    NFormView initView(NFormViewProperty viewProperty, ViewDispatcher viewDispatcher);

    NFormViewData getViewData();

    void setOnDataPassListener(DataActionListener dataActionListener);

    void handleRules();

    void setupView();

    RootView getNFormRootView();

}
