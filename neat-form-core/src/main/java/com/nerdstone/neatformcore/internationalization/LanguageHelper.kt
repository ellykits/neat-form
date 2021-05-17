package com.nerdstone.neatformcore.internationalization

import org.apache.commons.text.StringSubstitutor
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * Created by ndegwamartin on 25/04/2021.
 *
 * A class containing helper methods for internationalization and localization support
 */
object LanguageHelper {

    fun getBundleStringSubstitutor(baseName: String, locale: Locale): StringSubstitutor? {
        return try {
            val resourceBundle: ResourceBundle = ResourceBundle.getBundle(baseName, locale)
            StringSubstitutor(getLookupMap(resourceBundle), "{{", "}}")

        } catch (missingResourceException: MissingResourceException) {
            null
        }
    }

    private fun getLookupMap(bundle: ResourceBundle): HashMap<String, Any>? {
        bundle::class.memberProperties.forEach {
            if ("lookup" == it.name) {

                it.isAccessible = true
                return it.getter.call(bundle) as HashMap<String, Any>
            }
        }
        return null
    }

    fun getBundleNameFromFileSource(fileSource: String): String = fileSource.substring(
        fileSource.lastIndexOf('/') + 1,
        fileSource.lastIndexOf('.')
    )

}