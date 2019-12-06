package com.nerdstone.neatformcore.views.containers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.nerdstone.neatformcore.domain.listeners.DataActionListener
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.rules.NeatFormValidator
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.MultiChoiceCheckBoxViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.handlers.ViewVisibilityChangeHandler

class MultiChoiceCheckBox : LinearLayout, NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override var dataActionListener: DataActionListener? = null
    override var visibilityChangeListener: VisibilityChangeListener? =
        ViewVisibilityChangeHandler.INSTANCE
    override val viewBuilder = MultiChoiceCheckBoxViewBuilder(this)
    override val viewDetails = NFormViewDetails(this)
    override var formValidator: FormValidator = NeatFormValidator.INSTANCE

    init {
        orientation = VERTICAL
    }

    constructor(context: Context) : super(context)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun initView(
        viewProperty: NFormViewProperty,
        viewDispatcher: ViewDispatcher
    ): NFormView {
        ViewUtils.setupView(this, viewProperty, viewDispatcher)
        return this
    }

    override fun trackRequiredField() = ViewUtils.handleRequiredStatus(this)

    override fun resetValueWhenHidden() {
        viewBuilder.resetCheckBoxValues()
    }

    override fun validateValue(): Boolean =
        formValidator.validateLabeledField(this)

    override fun setVisibility(visibility: Int) {
        super.setVisibility( visibility)
        visibilityChangeListener?.onVisibilityChanged(this, visibility)
    }
}