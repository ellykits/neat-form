package com.opensrp.neatformcore.views.controls;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import com.opensrp.neatformcore.domain.model.NFormViewData;
import com.opensrp.neatformcore.domain.model.NFormViewOption;
import com.opensrp.neatformcore.domain.model.NFormViewProperty;
import com.opensrp.neatformcore.domain.view.DataPassListener;
import com.opensrp.neatformcore.domain.view.NFormView;
import com.opensrp.neatformcore.views.data.ViewDataHandler;

public class CheckboxNFormView extends AppCompatCheckBox implements NFormView {

    private static String TAG = CheckboxNFormView.class.getCanonicalName();

    private DataPassListener dataPassListener;

    private NFormViewProperty viewProperty;

    private NFormViewOption viewOption;

    public CheckboxNFormView(Context context) {
        super(context);
    }

    public CheckboxNFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckboxNFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
}
