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
import com.nerdstone.neatformcore.rules.NeatFormValidator
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.SpinnerViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewVisibilityChangeHandler

class SpinnerNFormView : LinearLayout, NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override var dataActionListener: DataActionListener? = null
    override val viewBuilder = SpinnerViewBuilder(this)
    override var viewDetails = NFormViewDetails(this)
    override var formValidator: FormValidator = NeatFormValidator.INSTANCE
    override var visibilityChangeListener: VisibilityChangeListener? =
        ViewVisibilityChangeHandler.INSTANCE

    init {
        orientation = VERTICAL
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun trackRequiredField() = ViewUtils.handleRequiredStatus(this)

    override fun resetValueWhenHidden() = viewBuilder.resetSpinnerValue()

    override fun setValue(value: Any, enabled: Boolean) {
        TODO("Not yet implemented")
    }

    override fun validateValue() = formValidator.validateField(this).first

    override fun setVisibility(visibility: Int) {
        super.setVisibility( visibility)
        visibilityChangeListener?.onVisibilityChanged(this, visibility)
    }
}