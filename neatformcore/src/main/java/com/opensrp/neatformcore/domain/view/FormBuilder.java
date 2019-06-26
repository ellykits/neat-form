package com.opensrp.neatformcore.domain.view;

import android.content.Context;
import android.view.ViewGroup;

import com.opensrp.neatformcore.domain.model.NForm;
import com.opensrp.neatformcore.views.data.ViewDataHandler;

public interface FormBuilder {
    void setMainLayout(ViewGroup mainLayout);

    ViewGroup getMainLayout();

    NForm getForm(String source);

    void addFormViews(NForm form, Context context);

    void setViewDataHandler(ViewDataHandler viewDataHandler);
}
