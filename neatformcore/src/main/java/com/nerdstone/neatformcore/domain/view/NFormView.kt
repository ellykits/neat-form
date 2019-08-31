package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

interface NFormView {

    val viewDetails: NFormViewDetails

    val viewData: NFormViewData

    val nFormRootView: RootView

    fun initView(viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher): NFormView

    fun setOnDataPassListener(dataActionListener: DataActionListener)

    fun setupView()

}
