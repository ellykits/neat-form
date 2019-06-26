package com.opensrp.neatformcore.domain.view;

import com.opensrp.neatformcore.domain.model.NFormViewData;
import com.opensrp.neatformcore.domain.model.NFormViewOption;
import com.opensrp.neatformcore.domain.model.NFormViewProperty;
import com.opensrp.neatformcore.views.data.ViewDataHandler;

public interface NFormView {
    NFormViewOption getViewOption();

    NFormView initView(NFormViewProperty viewProperty, ViewDataHandler viewDataHandler);

    NFormViewData getViewData();

    void setOnDataPassListener(DataPassListener dataPassListener);
}
