package com.nerdstone.neatformcore.views.containers;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty;
import com.nerdstone.neatformcore.domain.view.NFormView;
import com.nerdstone.neatformcore.domain.view.RootView;
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher;

import java.util.List;

import static com.nerdstone.neatformcore.utils.NFormViewUtils.createViews;

public class VerticalRootView extends LinearLayout implements RootView {

    public VerticalRootView(Context context) {
        super(context);
        setupView();
    }

    public VerticalRootView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    @Override
    public RootView initRootView() {
        return this;
    }

    @Override
    public void addChild(NFormView nFormView) {
        this.addView(nFormView.getViewDetails().getView());
    }

    @Override
    public void addChildren(List<NFormViewProperty> viewProperties, ViewDispatcher viewDispatcher) {
        createViews(this, viewProperties, getContext(), viewDispatcher);
    }

    @Override
    public List<NFormViewData> getViewsData() {
        return null;
    }

    @Override
    public void setupView() {
        this.setOrientation(VERTICAL);
    }
}
