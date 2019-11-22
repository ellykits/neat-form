package com.nerdstone.neatformcore.form.json

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import com.nerdstone.neatformcore.domain.model.NForm
import java.io.StringReader

object JsonFormParser {

    fun parseJson(json: String?): NForm? {
        if (json != null && json.isNotEmpty()) {
            val gson = GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                .create()
            return gson.fromJson<NForm>(JsonReader(StringReader(json)), NForm::class.java)
        }
        return null
    }

}
