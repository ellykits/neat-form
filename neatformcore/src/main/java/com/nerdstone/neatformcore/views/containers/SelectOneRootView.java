package com.nerdstone.neatformcore.views.containers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.NFormViewProperty;
import com.nerdstone.neatformcore.domain.view.NFormView;
import com.nerdstone.neatformcore.domain.view.RootView;
import com.nerdstone.neatformcore.views.data.ViewDataHandler;

import java.util.List;

public class SelectOneRootView extends RadioGroup implements RootView {

    public SelectOneRootView(Context context) {
        super(context);
        setupView();
    }

    public SelectOneRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    @Override
    public RootView initRootView() {
        return this;
    }

    @Override
    public void addChild(NFormView nFormView) {

    }

    @Override
    public void addChildren(List<NFormViewProperty> viewProperties,
                            ViewDataHandler viewDataHandler) {

    }

    @Override
    public List<NFormViewData> getViewsData() {
        return null;
    }

    @Override
    public void setupView() {

    }
}
