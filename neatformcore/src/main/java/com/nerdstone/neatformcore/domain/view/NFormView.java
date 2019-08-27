package com.nerdstone.neatformcore.domain.view;

import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.NFormViewDetails;
import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty;
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher;

public interface NFormView {

    NFormViewDetails getViewDetails();

    NFormView initView(NFormViewProperty viewProperty, ViewDispatcher viewDispatcher);

    NFormViewData getViewData();

    void setOnDataPassListener(DataActionListener dataActionListener);

    void setupView();

    RootView getNFormRootView();

}
