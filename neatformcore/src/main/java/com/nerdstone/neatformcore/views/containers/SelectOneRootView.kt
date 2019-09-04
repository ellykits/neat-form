package com.nerdstone.neatformcore.views.containers

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioGroup

import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class SelectOneRootView : RadioGroup, RootView {

    override val viewsData: List<NFormViewData> = listOf()

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupView()
    }

    override fun initRootView(): RootView {
        return this
    }

    override fun addChild(nFormView: NFormView) {

    }

    override fun addChildren(viewProperties: List<NFormViewProperty>,
                             viewDispatcher: ViewDispatcher) {

    }

    override fun setupView() {

    }
}
