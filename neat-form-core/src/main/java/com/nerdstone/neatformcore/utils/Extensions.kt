package com.nerdstone.neatformcore.utils

import android.view.View
import android.view.ViewGroup


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