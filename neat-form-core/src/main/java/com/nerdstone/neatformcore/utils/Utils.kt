package com.nerdstone.neatformcore.utils

import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.GsonBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import java.util.*


object ThemeColor {
    const val COLOR_ACCENT = "colorAccent"
    const val COLOR_PRIMARY = "colorPrimary"
    const val COLOR_PRIMARY_DARK = "colorPrimaryDark"
}

object Utils {

    fun <T : Enum<T>> convertEnumToSet(clazz: Class<T>): HashSet<String> {
        return EnumSet.allOf(clazz).map { it.name }.toHashSet()
    }

    fun pxToDp(value: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics
        ).toInt()
    }

    fun extractKeyValue(key: String): Pair<String, String> {
        val keyDataType = ViewUtils.splitText(key, ":")
        return Pair(keyDataType.first().trim(), keyDataType.last().trim())
    }

    fun isFieldRequired(nFormView: NFormView): Boolean {
        nFormView.viewProperties.requiredStatus?.also {
            val isRequired = extractKeyValue(nFormView.viewProperties.requiredStatus!!)
                .first.toLowerCase(Locale.getDefault())
            return isRequired == "yes" || isRequired == "true"
        }
        return false
    }

    fun hideSoftKeyBoard(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getThemeColor(context: Context, themeColor: String): Int {
        val colorAttr: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            when (themeColor) {
                ThemeColor.COLOR_ACCENT -> android.R.attr.colorAccent
                ThemeColor.COLOR_PRIMARY -> android.R.attr.colorPrimary
                ThemeColor.COLOR_PRIMARY_DARK -> android.R.attr.colorPrimaryDark
                else -> android.R.attr.colorAccent
            }
        } else {
            context.resources.getIdentifier(themeColor, "attr", context.packageName)
        }
        val outValue = TypedValue()
        context.theme.resolveAttribute(colorAttr, outValue, true)
        return outValue.data
    }

    fun getJsonFromModel(model: Any): String = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create().toJson(model)
}