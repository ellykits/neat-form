package com.opensrp.neatformcore.views.data;

import android.util.Log;

import com.opensrp.neatformcore.domain.model.NFormViewOption;
import com.opensrp.neatformcore.domain.view.DataPassListener;

import org.jeasy.rules.api.Facts;

public class ViewDataHandler implements DataPassListener {

    private static Facts facts = new Facts();

    private static final String TAG = ViewDataHandler.class.getCanonicalName();

    @Override
    public void onPassData(NFormViewOption viewOption) {
        facts.put(viewOption.getName(), viewOption.getValue());
        Log.i(TAG, "Passed data {name: " + viewOption.getName() + " , value: " + viewOption.getValue() + "}");
    }

}
