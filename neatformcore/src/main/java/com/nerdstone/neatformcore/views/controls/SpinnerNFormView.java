package com.nerdstone.neatformcore.views.controls;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;

import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.NFormViewDetails;
import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty;
import com.nerdstone.neatformcore.domain.view.DataActionListener;
import com.nerdstone.neatformcore.domain.view.NFormView;
import com.nerdstone.neatformcore.domain.view.RootView;
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher;

public class SpinnerNFormView extends AppCompatSpinner implements NFormView {

    private static String TAG = SpinnerNFormView.class.getCanonicalName();
    private NFormViewDetails viewDetails;
    private DataActionListener dataActionListener;

    public SpinnerNFormView(Context context) {
        super(context);
        setupView();
    }

    public SpinnerNFormView(Context context, int mode) {
        super(context, mode);
        setupView();
    }

    public SpinnerNFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public SpinnerNFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    public SpinnerNFormView(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
        setupView();
    }

    public SpinnerNFormView(Context context, AttributeSet attrs, int defStyleAttr, int mode,
                            Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
        setupView();
    }

    @Override
    public NFormViewDetails getViewDetails() {
        return this.viewDetails;
    }

    @Override
    public NFormView initView(NFormViewProperty viewProperty, ViewDispatcher viewDispatcher) {
        this.viewDetails = new NFormViewDetails(this);
        setOnDataPassListener(viewDispatcher);
        return this;
    }

    @Override
    public NFormViewData getViewData() {
        return new NFormViewData();
    }

    @Override
    public void setOnDataPassListener(DataActionListener dataActionListener) {
        if (this.dataActionListener == null) {
            this.dataActionListener = dataActionListener;
        }
    }

    @Override
    public void setupView() {

    }

    @Override
    public RootView getNFormRootView() {
        return (RootView) this.getParent();
    }
}
