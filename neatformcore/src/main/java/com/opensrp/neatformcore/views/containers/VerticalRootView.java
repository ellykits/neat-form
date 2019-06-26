package com.opensrp.neatformcore.views.containers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.opensrp.neatformcore.domain.model.NFormViewData;
import com.opensrp.neatformcore.domain.model.NFormViewProperty;
import com.opensrp.neatformcore.domain.view.NFormView;
import com.opensrp.neatformcore.domain.view.RootView;
import com.opensrp.neatformcore.views.data.ViewDataHandler;

import java.util.List;

import static com.opensrp.neatformcore.utils.NFormViewUtils.createViews;

public class VerticalRootView extends LinearLayout implements RootView {

    public VerticalRootView(Context context) {
        super(context);
    }

    public VerticalRootView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public RootView initRootView() {
        this.setOrientation(VERTICAL);
        return this;
    }

    @Override
    public void addChild(NFormView nFormView) {
        this.addView(nFormView.getViewOption().getView());
    }

    @Override
    public void addChildren(List<NFormViewProperty> viewProperties, ViewDataHandler viewDataHandler) {
        createViews(this, viewProperties, getContext(), viewDataHandler);
    }

    @Override
    public List<NFormViewData> getViewsData() {

        return null;
    }

}
