package com.nerdstone.neatformcore.domain.view;

import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.NFormViewOption;
import com.nerdstone.neatformcore.domain.model.NFormViewProperty;
import com.nerdstone.neatformcore.views.data.ViewDataHandler;

public interface NFormView {

    NFormViewOption getViewOption();

    NFormView initView(NFormViewProperty viewProperty, ViewDataHandler viewDataHandler);

    NFormViewData getViewData();

    void setOnDataPassListener(DataPassListener dataPassListener);

    void handleRules();

    void setupView();

}
