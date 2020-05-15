package com.nerdstone.neatformcore.form.json

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import java.io.StringReader

object JsonParser {
    @Throws(Exception::class)
    inline fun <reified T> parseJson(json: String?): T? =
        if (!json.isNullOrEmpty())
        GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
            .create()
            .fromJson<T>(JsonReader(StringReader(json)), T::class.java)
    else null

    inline fun <reified T> Gson.parseJson(json: String): T =
        fromJson(JsonReader(StringReader(json)), object : TypeToken<T>() {}.type)

}
