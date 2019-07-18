package com.nerdstone.neatformcore.domain.view;

import android.content.Context;
import android.view.ViewGroup;

import com.nerdstone.neatformcore.domain.model.form.NForm;
import com.nerdstone.neatformcore.rules.RulesFactory;
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher;

public interface FormBuilder {

    ViewGroup getMainLayout();

    void setMainLayout(ViewGroup mainLayout);

    NForm getForm(String source);

    void createFormViews(Context context);

    void setViewDispatcher(ViewDispatcher viewDispatcher);

    void registerFormRules(Context context, RulesFactory.RulesFileType rulesFileType);

    void freeResources();
}