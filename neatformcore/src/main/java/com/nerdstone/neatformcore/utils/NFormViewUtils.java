package com.nerdstone.neatformcore.utils;

import android.content.Context;

import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty;
import com.nerdstone.neatformcore.domain.view.RootView;
import com.nerdstone.neatformcore.utils.Constants.ViewType;
import com.nerdstone.neatformcore.views.controls.EditTextNFormView;
import com.nerdstone.neatformcore.views.controls.SpinnerNFormView;
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NFormViewUtils {

    public static void createViews(RootView rootView, List<NFormViewProperty> viewProperties,
                                   Context context, ViewDispatcher viewDispatcher) {

        for (NFormViewProperty viewProperty : viewProperties) {
            registerSubjects(splitText(viewProperty.getSubjects(), ","), viewProperty, viewDispatcher);
            switch (viewProperty.getType()) {
                case ViewType.EDIT_TEXT:
                    rootView.addChild(new EditTextNFormView(context).initView(viewProperty, viewDispatcher));
                    break;
                case ViewType.SPINNER:
                    rootView.addChild(new SpinnerNFormView(context).initView(viewProperty, viewDispatcher));
                    break;
            }
        }
    }

    private static void registerSubjects(List<String> subjects, NFormViewProperty viewProperty,
                                         ViewDispatcher viewDispatcher) {
        Map<String, Set<String>> subjectsRegistry = viewDispatcher.getRulesHandler()
                .getRulesFactory().getSubjectsRegistry();
        if (subjectsRegistry != null) {
            for (String subject : subjects) {
                if (!subjectsRegistry.containsKey(subject)) {
                    subjectsRegistry.put(subject, new HashSet<>());
                }
                subjectsRegistry.get(subject).add(viewProperty.getName());
            }
        }

    }

    public static List<String> splitText(String text, String delimiter) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(text.split(delimiter));
    }

}
