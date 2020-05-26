package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import java.io.Serializable

interface RootView : Serializable {

    var formBuilder: FormBuilder

    fun initRootView(formBuilder: FormBuilder): RootView

    fun addChild(nFormView: NFormView)

    fun addChildren(
        viewProperties: List<NFormViewProperty>, formBuilder: FormBuilder,
        buildFromLayout: Boolean = false
    )
}
