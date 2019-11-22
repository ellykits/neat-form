package com.nerdstone.neatformcore.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.nerdstone.neatformcore.domain.view.NFormView
import java.util.*


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
        val isRequired = extractKeyValue(nFormView.viewProperties.requiredStatus!!)
            .first.toLowerCase(Locale.getDefault())
        return isRequired == "yes" || isRequired == "true"
    }

    fun hideSoftKeyBoard(view:View) {
        val inputMethodManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}