package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.nerdstone.neatformcore.domain.listeners.DataActionListener
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.handleRequiredStatus
import com.nerdstone.neatformcore.views.builders.NumberSelectorViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewVisibilityChangeHandler

class NumberSelectorNFormView : LinearLayout, NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override lateinit var formValidator: FormValidator
    override var dataActionListener: DataActionListener? = null
    override val viewBuilder = NumberSelectorViewBuilder(this)
    override var viewDetails = NFormViewDetails(this)
    override var visibilityChangeListener: VisibilityChangeListener? = ViewVisibilityChangeHandler
    override var initialValue: Any? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        orientation = VERTICAL
    }

    override fun resetValueWhenHidden() = viewBuilder.resetNumberSelectorValue()

    override fun trackRequiredField() = handleRequiredStatus()

    override fun setValue(value: Any, enabled: Boolean) {
        initialValue = value
        viewBuilder.setValue(value, enabled)
    }

    override fun validateValue() = formValidator.validateLabeledField(this)

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        visibilityChangeListener?.onVisibilityChanged(this, visibility)
    }
}