package com.nerdstone.neatformcore.utils.internationalization

import org.apache.commons.text.StringSubstitutor
import java.util.*

/**
 * Created by ndegwamartin on 25/04/2021.
 *
 * A class containing helper methods for internationalization and localization support
 */
object LanguageHelper {

    @Throws(IllegalArgumentException::class, MissingResourceException::class)
    fun getBundleStringSubstitutor(baseName: String, locale: Locale): StringSubstitutor {
        val resourceBundle = ResourceBundle.getBundle(baseName, locale)
        return StringSubstitutor(getLookupMap(resourceBundle), "{{", "}}")
    }

    private fun getLookupMap(resourceBundle: ResourceBundle): MutableMap<String, Any> {
        val mutableMap = mutableMapOf<String, Any>()
        resourceBundle.keys.toList().forEach { mutableMap[it] = resourceBundle.getObject(it) }
        return mutableMap
    }

    fun getBundleNameFromFileSource(fileSource: String) = fileSource.run {
        substring(lastIndexOf('/') + 1, lastIndexOf('.'))
    }
}