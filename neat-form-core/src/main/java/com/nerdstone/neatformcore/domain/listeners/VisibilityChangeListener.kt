package com.nerdstone.neatformcore.domain.listeners

import android.view.View

interface VisibilityChangeListener {

    fun onVisibilityChanged(changedView: View, visibility: Int)

}