package com.nerdstone.neatformcore.utils.internationalization

import org.apache.commons.text.StringSubstitutor
import java.util.*

/**
 * Created by ndegwamartin on 25/04/2021.
 *
 * A class containing helper methods for internationalization and localization support
 */
object LanguageHelper {

    @Throws(IllegalArgumentException::class)
    fun getBundleStringSubstitutor(resourceBundle: ResourceBundle): StringSubstitutor {
        val lookup = mutableMapOf<String, Any>()
        resourceBundle.keys.toList().forEach { lookup[it] = resourceBundle.getObject(it) }
        return StringSubstitutor(lookup, "{{", "}}")
    }

    fun getBundleNameFromFileSource(fileSource: String) = fileSource.run {
        substring(lastIndexOf('/') + 1, lastIndexOf('.'))
    }
}