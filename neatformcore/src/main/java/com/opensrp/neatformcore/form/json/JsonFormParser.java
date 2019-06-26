package com.opensrp.neatformcore.form.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.opensrp.neatformcore.domain.model.NForm;

import java.io.StringReader;

public class JsonFormParser {

    public static NForm parseJson(String json) {
        if (json != null && !json.isEmpty()) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                    .create();
            return gson.fromJson(new JsonReader(new StringReader(json)), NForm.class);
        }
        return null;
    }

}
