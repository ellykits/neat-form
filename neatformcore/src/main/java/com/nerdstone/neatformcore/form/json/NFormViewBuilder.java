package com.nerdstone.neatformcore.form.json;

import android.view.View;

import com.nerdstone.neatformcore.utils.Constants;

import java.util.Map;

public class NFormViewBuilder {
    public static void makeView(Map<String, Object> attributes, View view, String type) {
        switch (type) {
            case Constants.ViewType.EDIT_TEXT:
                makeEditText(attributes, view);
                break;
        }
    }

    private static void makeEditText(Map<String, Object> attributes, View view) {

    }


}
