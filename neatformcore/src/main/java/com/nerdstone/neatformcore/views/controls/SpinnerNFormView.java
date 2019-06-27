package com.nerdstone.neatformcore.views.controls;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;

import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.NFormViewOption;
import com.nerdstone.neatformcore.domain.model.NFormViewProperty;
import com.nerdstone.neatformcore.domain.view.DataPassListener;
import com.nerdstone.neatformcore.domain.view.NFormView;
import com.nerdstone.neatformcore.views.data.ViewDataHandler;

public class SpinnerNFormView extends AppCompatSpinner implements NFormView {

    private static String TAG = SpinnerNFormView.class.getCanonicalName();

    private NFormViewOption viewOption;

    private DataPassListener dataPassListener;

    public SpinnerNFormView(Context context) {
        super(context);
    }

    public SpinnerNFormView(Context context, int mode) {
        super(context, mode);
    }

    public SpinnerNFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerNFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SpinnerNFormView(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public SpinnerNFormView(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);
    }

    @Override
    public NFormViewOption getViewOption() {
        return this.viewOption;
    }

    @Override
    public NFormView initView(NFormViewProperty viewProperty, ViewDataHandler viewDataHandler) {
        this.viewOption = new NFormViewOption(this);
        setOnDataPassListener(viewDataHandler);
        return this;
    }

    @Override
    public NFormViewData getViewData() {
        return new NFormViewData();
    }

    @Override
    public void setOnDataPassListener(DataPassListener dataPassListener) {
        if(this.dataPassListener == null) {
            this.dataPassListener = dataPassListener;
        }
    }

    @Override
    public void handleRules() {

    }
}
