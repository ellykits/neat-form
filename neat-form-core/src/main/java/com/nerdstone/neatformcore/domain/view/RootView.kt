package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import java.io.Serializable

interface RootView: Serializable{

    var formBuilder: FormBuilder

    fun initRootView(formBuilder: FormBuilder): RootView

    fun addChild(nFormView: NFormView)

    fun addChildren(viewProperties: List<NFormViewProperty>, viewDispatcher: ViewDispatcher,buildFromLayout: Boolean=false)

}
