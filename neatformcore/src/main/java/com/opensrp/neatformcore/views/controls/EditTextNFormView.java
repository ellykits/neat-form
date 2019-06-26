package com.opensrp.neatformcore.views.controls;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.opensrp.neatformcore.domain.model.NFormViewData;
import com.opensrp.neatformcore.domain.model.NFormViewOption;
import com.opensrp.neatformcore.domain.model.NFormViewProperty;
import com.opensrp.neatformcore.domain.view.DataPassListener;
import com.opensrp.neatformcore.domain.view.NFormView;
import com.opensrp.neatformcore.utils.Constants;
import com.opensrp.neatformcore.views.data.ViewDataHandler;

import static com.opensrp.neatformcore.form.json.NFormViewBuilder.makeView;

public class EditTextNFormView extends AppCompatEditText implements NFormView {

    private static String TAG = EditTextNFormView.class.getCanonicalName();

    private NFormViewProperty viewProperty;

    private DataPassListener dataPassListener;

    private NFormViewOption viewOption;

    public EditTextNFormView(Context context) {
        super(context);
    }

    public EditTextNFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextNFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public NFormViewOption getViewOption() {
        return viewOption;
    }

    @Override
    public NFormView initView(NFormViewProperty viewProperty, ViewDataHandler viewDataHandler) {
        this.viewOption = new NFormViewOption(this);
        if (viewProperty != null) {
            viewOption.setName(viewProperty.getName());

            if (viewProperty.getViewAttributes() != null) {
                makeView(viewProperty.getViewAttributes(), this, Constants.ViewType.EDIT_TEXT);
            }
        }
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
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (viewOption != null && this.dataPassListener != null) {
            this.viewOption.setValue(text);
            this.dataPassListener.onPassData(viewOption);
        }
    }
}
