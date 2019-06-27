package com.nerdstone.neatformcore.views.data;

import android.util.Log;

import com.nerdstone.neatformcore.domain.model.NFormViewOption;
import com.nerdstone.neatformcore.domain.view.DataPassListener;

import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewDataHandler implements DataPassListener {

    private static Facts facts = new Facts();

    private static final String TAG = ViewDataHandler.class.getCanonicalName();

    @Override
    public void onPassData(NFormViewOption viewOption) {
        facts.put(viewOption.getName(), viewOption.getValue());
        String regex = "\\{(.*?)\\}";
        String test = "This is a {blue_gam} string with {gest_age_openmrs} special words. {gest_age}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(test);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        for (String key : matches) {
            Log.i(TAG, "Key: " + key.replace("{", "").replace("}",""));
        }

        Log.i(TAG, "Passed data {name: " + viewOption.getName() + " , value: " + viewOption.getValue() + "}");
    }

}
