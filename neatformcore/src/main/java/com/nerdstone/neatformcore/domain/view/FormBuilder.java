package com.nerdstone.neatformcore.domain.view;

import android.content.Context;
import android.view.ViewGroup;

import com.nerdstone.neatformcore.domain.model.NForm;
import com.nerdstone.neatformcore.views.data.ViewDataHandler;

public interface FormBuilder {
    void setMainLayout(ViewGroup mainLayout);

    ViewGroup getMainLayout();

    NForm getForm(String source);

    void addFormViews(NForm form, Context context);

    void setViewDataHandler(ViewDataHandler viewDataHandler);
}
