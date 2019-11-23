package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import androidx.appcompat.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.View
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.EditTextViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class EditTextNFormView : AppCompatEditText, NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override var dataActionListener: DataActionListener? = null
    override val viewBuilder = EditTextViewBuilder(this)
    override var viewDetails = NFormViewDetails(this)
    override val nFormRootView get() = this.parent as RootView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun initView(viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher)
            : NFormView {
        ViewUtils.setupView(this, viewProperty, viewDispatcher)
        return this
    }

    override fun onTextChanged(
        text: CharSequence, start: Int, lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if (text.isNotEmpty()) {
            dataActionListener?.also {
                this.viewDetails.value = text.toString()
                it.onPassData(viewDetails)
            }
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        resetValueWhenHidden()
    }

    override fun resetValueWhenHidden() {
        if (visibility == View.GONE) {
            setText("")
        }
    }
}