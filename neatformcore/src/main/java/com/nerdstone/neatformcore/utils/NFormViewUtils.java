package com.nerdstone.neatformcore.utils;

import android.content.Context;

import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty;
import com.nerdstone.neatformcore.domain.view.RootView;
import com.nerdstone.neatformcore.views.controls.EditTextNFormView;
import com.nerdstone.neatformcore.views.controls.SpinnerNFormView;
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher;

import java.util.List;

public class NFormViewUtils {

    public static void createViews(RootView rootView, List<NFormViewProperty> viewProperties,
                                   Context context, ViewDispatcher viewDispatcher) {

        for (NFormViewProperty viewProperty : viewProperties) {
            switch (viewProperty.getType()) {
                case Constants.ViewType.EDIT_TEXT:
                    rootView.addChild(new EditTextNFormView(context).initView(viewProperty, viewDispatcher));
                    break;
                case Constants.ViewType.SPINNER:
                    rootView.addChild(new SpinnerNFormView(context).initView(viewProperty, viewDispatcher));
                    break;
            }
        }
    }

}
