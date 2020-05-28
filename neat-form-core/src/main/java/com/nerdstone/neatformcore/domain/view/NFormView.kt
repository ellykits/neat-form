package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.listeners.DataActionListener
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty

/**
 * @author Elly Nerdstone
 *
 * very view must implement this interface. This contract basically defines all the interactions
 * between fields. The form builder needs to be aware of how the fields are going to handle
 * validation, required values and how they shall pass their values.
 */
interface NFormView {

    var dataActionListener: DataActionListener?

    var visibilityChangeListener: VisibilityChangeListener?

    val viewDetails: NFormViewDetails

    var viewProperties: NFormViewProperty

    val viewBuilder: ViewBuilder

    var formValidator: FormValidator

    var initialValue: Any?

    /**
     * This function is called to reset the value of the field when it is hidden
     */
    fun resetValueWhenHidden()

    /**
     * This function is to implement field validation and it returns a [Boolean] value.
     */
    fun validateValue(): Boolean

    /**
     * This function is used by fields to handle their required status
     */
    fun trackRequiredField()

    /**
     * This method sets [value] of the field. And it also accepts an optional parameter called
     * [enabled] that is used to indicate whether you want the field to be disabled or not. Use the
     * option if you want to make the field readonly.
     */
    fun setValue(value: Any, enabled: Boolean = true)
}
