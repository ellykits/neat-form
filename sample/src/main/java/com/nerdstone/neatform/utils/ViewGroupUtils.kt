package com.nerdstone.neatform.utils

import android.view.View
import android.view.ViewGroup

/**
 * Created by cozej4 on 2019-11-25.
 *
 * @author cozej4 https://github.com/cozej4
 */
object ViewGroupUtils {
    fun getParent(view: View): ViewGroup? {
        return view.parent as ViewGroup
    }

    fun removeView(view: View) {
        val parent = getParent(view)
        parent?.removeView(view)
    }

    fun replaceView(currentView: View, newView: View) {
        val parent = getParent(currentView) ?: return
        val index = parent.indexOfChild(currentView)
        removeView(currentView)
        parent.addView(newView, index)
    }
}