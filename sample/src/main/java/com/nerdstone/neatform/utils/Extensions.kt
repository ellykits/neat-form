package com.nerdstone.neatform.utils

import android.view.View
import android.view.ViewGroup

fun replaceView(oldView: View, newView: View) {
    (oldView.parent as ViewGroup).also {
        it.removeView(oldView)
        it.addView(newView, it.indexOfChild(oldView))
    }
}