package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout
import com.nerdstone.neatformcore.domain.listeners.DataActionListener
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.handleRequiredStatus
import com.nerdstone.neatformcore.utils.setReadOnlyState
import com.nerdstone.neatformcore.views.builders.DateTimePickerViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewVisibilityChangeHandler

class DateTimePickerNFormView : TextInputLayout, NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override lateinit var formValidator: FormValidator
    override var dataActionListener: DataActionListener? = null
    override var visibilityChangeListener: VisibilityChangeListener? = ViewVisibilityChangeHandler
    override val viewBuilder = DateTimePickerViewBuilder(this)
    override val viewDetails = NFormViewDetails(this)
    override var initialValue: Any? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun resetValueWhenHidden() = viewBuilder.resetDatetimePickerValue()

    override fun trackRequiredField() = handleRequiredStatus()

    override fun validateValue(): Boolean {
        val validationPair = formValidator.validateField(this)
        if (!validationPair.first) {
            this.error = validationPair.second
        } else this.error = null
        return validationPair.first
    }

    override fun setValue(value: Any, enabled: Boolean) {
        initialValue = value
        when (value) {
            is Double -> viewBuilder.selectedDate.timeInMillis = value.toLong()
            is Long -> viewBuilder.selectedDate.timeInMillis = value
        }
        viewBuilder.updateViewData()
        setReadOnlyState(enabled)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        visibilityChangeListener?.onVisibilityChanged(this, visibility)
    }
}