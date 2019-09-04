package com.nerdstone.neatformcore.views.containers

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.NFormViewUtils.createViews
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class VerticalRootView : LinearLayout, RootView {

    override val viewsData: List<NFormViewData> = listOf()

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setupView()
    }

    override fun initRootView(): RootView {
        return this
    }

    override fun addChild(nFormView: NFormView) {
        this.addView(nFormView.viewDetails.view)
    }

    override fun addChildren(viewProperties: List<NFormViewProperty>, viewDispatcher: ViewDispatcher) {
        createViews(this, viewProperties, context, viewDispatcher)
    }

    override fun setupView() {
        this.orientation = VERTICAL
    }
}
