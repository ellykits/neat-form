package com.nerdstone.neatformcore.views.controls;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.NFormViewOption;
import com.nerdstone.neatformcore.domain.model.NFormViewProperty;
import com.nerdstone.neatformcore.domain.view.DataPassListener;
import com.nerdstone.neatformcore.domain.view.NFormView;
import com.nerdstone.neatformcore.views.data.ViewDataHandler;

public class CheckboxNFormView extends AppCompatCheckBox implements NFormView {

    private static String TAG = CheckboxNFormView.class.getCanonicalName();
    private DataPassListener dataPassListener;
    private NFormViewOption viewOption;

    public CheckboxNFormView(Context context) {
        super(context);
        setupView();
    }

    public CheckboxNFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public CheckboxNFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
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
        if (this.dataPassListener == null) {
            this.dataPassListener = dataPassListener;
        }
    }

    @Override
    public void handleRules() {

    }

    @Override
    public void setupView() {

    }
}
