@file:JvmName("NeatFormAndroidUtils")
package com.nerdstone.neatformcore.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.view.NFormView
import timber.log.Timber
import java.util.*

/***
 * Ported from this Gist on Github https://gist.github.com/orip/5566666
 */
fun View.getViewsByTagValue(tag: Int, value: Any): List<View> {
    val viewResults = mutableListOf<View>()
    if (this is ViewGroup) {
        (0 until this.childCount).forEach { i ->
            viewResults.addAll(this.getChildAt(i).getViewsByTagValue(tag, value))
        }
    }
    val tagValue = this.getTag(tag)
    if (value == tagValue) {
        viewResults.add(this)
    }
    return viewResults
}

/**
 * This method is used to animate the [View]
 */
fun View.animateView() {
    when (visibility) {
        View.VISIBLE -> animate().alpha(1.0f).duration = 800
        View.INVISIBLE, View.GONE -> animate().alpha(0.0f).duration = 800
    }
}

@Throws(Throwable::class)
fun Context.findViewWithKey(key: String): View? {
    val activityRootView =
        (this as Activity).findViewById<View>(android.R.id.content).rootView
    return activityRootView.findViewWithTag(key)
}

fun View.setReadOnlyState(enabled: Boolean) {
    isEnabled = enabled
    isFocusable = enabled
}


/**
 * This method updates the values of the fields with the provided [fieldValues]. Fields that should be disabled
 * are listed in the [readOnlyFields]
 */
fun Context.updateFieldValues(
    fieldValues: HashMap<String, NFormViewData>, readOnlyFields: MutableSet<String>
) {
    fieldValues.filterNot { entry -> entry.key.endsWith(Constants.RuleActions.CALCULATION) }
        .forEach { entry ->
            val view: View? = findViewWithKey(entry.key)
            entry.value.value?.let { realValue ->
                if (view != null) (view as NFormView).setValue(
                    realValue, !readOnlyFields.contains(entry.key)
                )
            }
            Timber.d("Updated field %s : %s", entry.key, entry.value.value)
        }
}

fun Context.pxToDp(value: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics
    ).toInt()
}

fun Context.pixelsToSp(px: Float): Float {
    val scaledDensity = resources.displayMetrics.scaledDensity
    return px / scaledDensity
}


fun View.hideSoftKeyBoard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.getThemeColor(themeColor: String): Int {
    val colorAttr: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        when (themeColor) {
            Constants.ThemeColor.COLOR_ACCENT -> android.R.attr.colorAccent
            Constants.ThemeColor.COLOR_PRIMARY -> android.R.attr.colorPrimary
            Constants.ThemeColor.COLOR_PRIMARY_DARK -> android.R.attr.colorPrimaryDark
            else -> android.R.attr.colorAccent
        }
    } else {
        resources.getIdentifier(themeColor, "attr", packageName)
    }
    val outValue = TypedValue()
    theme.resolveAttribute(colorAttr, outValue, true)
    return outValue.data
}

fun Context.createAlertDialog(title: String?, message: String): AlertDialog.Builder {
    return AlertDialog.Builder(this)
        .apply {
            if (title != null) setTitle(title)
            setMessage(message)
            create()
        }
}