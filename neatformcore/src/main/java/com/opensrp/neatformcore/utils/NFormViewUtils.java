package com.opensrp.neatformcore.utils;

import android.content.Context;

import com.opensrp.neatformcore.domain.model.NFormViewProperty;
import com.opensrp.neatformcore.domain.view.RootView;
import com.opensrp.neatformcore.views.controls.EditTextNFormView;
import com.opensrp.neatformcore.views.controls.SpinnerNFormView;
import com.opensrp.neatformcore.views.data.ViewDataHandler;

import java.util.List;

public class NFormViewUtils {
    public static void createViews(RootView rootView, List<NFormViewProperty> viewProperties,
                                   Context context, ViewDataHandler viewDataHandler) {

        for (NFormViewProperty viewProperty : viewProperties) {

            switch (viewProperty.getType()) {
                case Constants.ViewType.EDIT_TEXT:
                    rootView.addChild(new EditTextNFormView(context).initView(viewProperty, viewDataHandler));
                    break;
                case Constants.ViewType.SPINNER:
                    rootView.addChild(new SpinnerNFormView(context).initView(viewProperty, viewDataHandler));
                    break;
            }
        }
    }

}
