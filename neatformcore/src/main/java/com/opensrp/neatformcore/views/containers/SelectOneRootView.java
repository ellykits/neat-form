package com.opensrp.neatformcore.views.containers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.opensrp.neatformcore.domain.model.NFormViewData;
import com.opensrp.neatformcore.domain.model.NFormViewProperty;
import com.opensrp.neatformcore.domain.view.NFormView;
import com.opensrp.neatformcore.domain.view.RootView;
import com.opensrp.neatformcore.views.data.ViewDataHandler;

import java.util.List;

public class SelectOneRootView extends RadioGroup implements RootView {

    public SelectOneRootView(Context context) {
        super(context);
    }

    public SelectOneRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public RootView initRootView() {
        return this;
    }

    @Override
    public void addChild(NFormView nFormView) {

    }

    @Override
    public void addChildren(List<NFormViewProperty> viewProperties, ViewDataHandler viewDataHandler) {

    }

    @Override
    public List<NFormViewData> getViewsData() {
        return null;
    }

}
