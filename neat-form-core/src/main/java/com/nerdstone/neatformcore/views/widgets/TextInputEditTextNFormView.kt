package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.TextInputEditTextBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class TextInputEditTextNFormView : TextInputLayout, NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override var dataActionListener: DataActionListener? = null
    override val viewBuilder = TextInputEditTextBuilder(this)
    override var viewDetails = NFormViewDetails(this)
    override lateinit var formValidator: FormValidator

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun initView(viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher)
            : NFormView {
        ViewUtils.setupView(this, viewProperty, viewDispatcher)
        return this
    }


    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        resetValueWhenHidden()
    }

    override fun trackRequiredField() = ViewUtils.handleRequiredStatus(this)

    override fun resetValueWhenHidden() {
        if (visibility == View.GONE) {
            editText?.setText("")
        }
    }

    override fun validateValue(): Boolean {
        val validationPair = formValidator.validateField(this)
        if (!validationPair.first) {
            this.error = validationPair.second
        } else this.error = null
        return validationPair.first
    }
}