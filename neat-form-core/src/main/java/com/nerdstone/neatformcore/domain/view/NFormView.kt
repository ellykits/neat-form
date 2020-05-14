package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.listeners.DataActionListener
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty

interface NFormView {

    var dataActionListener: DataActionListener?

    var visibilityChangeListener: VisibilityChangeListener?

    val viewDetails: NFormViewDetails

    var viewProperties: NFormViewProperty

    val viewBuilder: ViewBuilder

    var formValidator: FormValidator

    fun resetValueWhenHidden()

    fun validateValue() : Boolean

    fun trackRequiredField()
}
