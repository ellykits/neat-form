package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

interface RootView {

    val viewsData: List<NFormViewData>

    fun initRootView(): RootView

    fun addChild(nFormView: NFormView)

    fun addChildren(viewProperties: List<NFormViewProperty>, viewDispatcher: ViewDispatcher)

}
