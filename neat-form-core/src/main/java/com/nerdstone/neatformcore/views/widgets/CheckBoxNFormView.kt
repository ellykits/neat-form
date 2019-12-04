package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.removeAsterisk
import com.nerdstone.neatformcore.views.builders.CheckBoxViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class CheckBoxNFormView : CheckBox, NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override var dataActionListener: DataActionListener? = null
    override val viewBuilder = CheckBoxViewBuilder(this)
    override val viewDetails = NFormViewDetails(this)
    override lateinit var formValidator: FormValidator

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun initView(
        viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher
    ): NFormView {
        ViewUtils.setupView(this, viewProperty, viewDispatcher)
        return this
    }


    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        dataActionListener?.also {
            if (checked) {
                this.viewDetails.value =
                    mutableMapOf(viewDetails.name to text.toString().removeAsterisk())
            } else {
                this.viewDetails.value = mutableMapOf(viewDetails.name to null)
            }
            it.onPassData(viewDetails)
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        resetValueWhenHidden()
    }

    override fun resetValueWhenHidden() {
        if (visibility == View.GONE && isChecked) {
            isChecked = false
        }
    }

    override fun validateValue(): Boolean {
        return formValidator.validateField(this).first
    }
}