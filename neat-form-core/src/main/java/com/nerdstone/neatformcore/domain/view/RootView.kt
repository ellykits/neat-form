package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import java.io.Serializable

interface RootView: Serializable{

    val viewsData: List<NFormViewData>

    fun initRootView(): RootView

    fun addChild(nFormView: NFormView)

    fun addChildren(viewProperties: List<NFormViewProperty>, viewDispatcher: ViewDispatcher,buildFromLayout: Boolean=false)

}
