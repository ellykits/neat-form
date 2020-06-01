package com.nerdstone.neatformcore.views.handlers

import android.view.View
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.ViewUtils

object ViewVisibilityChangeHandler: VisibilityChangeListener {

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        ViewUtils.animateView(changedView)
        if (changedView is NFormView) {
            if (visibility == View.GONE && changedView.viewDetails.value != null
                && changedView.initialValue == null
            ) {
                changedView.resetValueWhenHidden()
            }
            changedView.trackRequiredField()
        }
    }
}