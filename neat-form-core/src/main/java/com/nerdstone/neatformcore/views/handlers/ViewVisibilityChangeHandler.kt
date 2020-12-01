package com.nerdstone.neatformcore.views.handlers

import android.view.View
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.animateView
import com.nerdstone.neatformcore.utils.isNotNull

object ViewVisibilityChangeHandler : VisibilityChangeListener {

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        changedView.animateView()
        with(changedView) {
            if (this is NFormView) {
                if (visibility == View.GONE && viewDetails.value.isNotNull()
                    && initialValue == null
                ) {
                    resetValueWhenHidden()
                }
                trackRequiredField()
            }
        }
    }
}