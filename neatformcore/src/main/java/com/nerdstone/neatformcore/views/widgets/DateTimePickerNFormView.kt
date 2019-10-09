package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.DateTimePickerViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class DateTimePickerNFormView : TextInputLayout, NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override var dataActionListener: DataActionListener? = null
    override val viewBuilder = DateTimePickerViewBuilder(this)
    override val viewDetails = NFormViewDetails(this)
    override val nFormRootView get() = this.parent as RootView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun initView(
        viewProperty: NFormViewProperty,
        viewDispatcher: ViewDispatcher
    ): NFormView {
        ViewUtils.setupView(this, viewProperty, viewDispatcher)
        return this
    }

    override fun resetValueWhenHidden() {
        TODO("not implemented")
    }
}