package com.nerdstone.neatformcore.form.json;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.nerdstone.neatformcore.domain.model.NForm;
import com.nerdstone.neatformcore.domain.model.NFormContent;
import com.nerdstone.neatformcore.domain.view.FormBuilder;
import com.nerdstone.neatformcore.domain.view.RootView;
import com.nerdstone.neatformcore.views.containers.VerticalRootView;
import com.nerdstone.neatformcore.views.data.ViewDataHandler;

/***
 * @author Elly Nerdstone
 *
 */
public class JsonFormBuilder implements FormBuilder {

    private ViewGroup mainLayout;

    private ViewDataHandler viewDataHandler;

    public JsonFormBuilder(ViewGroup mainLayout) {
        this.mainLayout = mainLayout;
        viewDataHandler = new ViewDataHandler();
    }

    @Override
    public void setMainLayout(ViewGroup mainLayout) {
        this.mainLayout = mainLayout;
    }

    @Override
    public ViewGroup getMainLayout() {
        return this.mainLayout;
    }

    @Override
    public NForm getForm(String source) {
        return JsonFormParser.parseJson(source);
    }

    /***
     *
     * @param form the form that has been passed from file (YML, XML or JSON)
     * @param context android context
     */
    @Override
    public void addFormViews(NForm form, Context context) {
        if (form != null && form.getSteps() != null) {
            for (NFormContent formContent : form.getSteps()) {
                RootView rootView = new VerticalRootView(context);
                rootView.addChildren(formContent.getFields(), viewDataHandler);
                mainLayout.addView((View) rootView.initRootView());
            }
        }
    }

    @Override
    public void setViewDataHandler(ViewDataHandler viewDataHandler) {
        this.viewDataHandler = viewDataHandler;
    }
}
