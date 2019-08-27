package com.nerdstone.neatformcore.views.controls;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.nerdstone.neatformcore.domain.model.NFormViewData;
import com.nerdstone.neatformcore.domain.model.NFormViewDetails;
import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty;
import com.nerdstone.neatformcore.domain.view.DataActionListener;
import com.nerdstone.neatformcore.domain.view.NFormView;
import com.nerdstone.neatformcore.domain.view.RootView;
import com.nerdstone.neatformcore.utils.Constants;
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher;

import static com.nerdstone.neatformcore.form.json.NFormViewBuilder.makeView;
import static com.nerdstone.neatformcore.utils.NFormViewUtils.splitText;

public class EditTextNFormView extends AppCompatEditText implements NFormView {

    private static String TAG = EditTextNFormView.class.getCanonicalName();
    private DataActionListener dataActionListener;
    private NFormViewDetails viewDetails;
    private NFormViewProperty viewProperties;

    public EditTextNFormView(Context context) {
        super(context);
        setupView();
    }

    public EditTextNFormView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }

    public EditTextNFormView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    @Override
    public NFormViewDetails getViewDetails() {
        return viewDetails;
    }

    @Override
    public NFormView initView(NFormViewProperty viewProperty, ViewDispatcher viewDispatcher) {
        this.viewDetails = new NFormViewDetails(this);
        this.viewProperties = viewProperty;

        if (viewProperty != null) {
            viewDetails.setName(viewProperty.getName());

            viewDetails.setSubjects(splitText(viewProperty.getSubjects(), ","));

            if (viewProperty.getViewAttributes() != null) {
                makeView(viewProperty.getViewAttributes(), this, Constants.ViewType.EDIT_TEXT);
            }
        }
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
        //TODO add implementation for setting up edit text
    }

    @Override
    public RootView getNFormRootView() {
        return (RootView) this.getParent();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (viewDetails != null && this.dataActionListener != null) {
            this.viewDetails.setValue(text);
            this.dataActionListener.onPassData(viewDetails);
        }
    }
}
