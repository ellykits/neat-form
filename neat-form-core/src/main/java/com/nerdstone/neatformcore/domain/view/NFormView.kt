package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

interface NFormView {

    var dataActionListener: DataActionListener?

    val viewDetails: NFormViewDetails

    val nFormRootView: RootView

    var viewProperties: NFormViewProperty

    val viewBuilder: ViewBuilder

    fun initView(viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher): NFormView

    fun resetValueWhenHidden()

    fun validateValue() : Boolean
}
