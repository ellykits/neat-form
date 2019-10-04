package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.View
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.domain.view.RulesHandler
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.EditTextViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class EditTextNFormView : AppCompatEditText, NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override var dataActionListener: DataActionListener? = null
    override val viewBuilder: EditTextViewBuilder = EditTextViewBuilder(this)
    override var viewDetails: NFormViewDetails = NFormViewDetails(this)
    override val viewData get() = NFormViewData()
    override val nFormRootView get() = this.parent as RootView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun initView(viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher)
            : NFormView {
        ViewUtils.setupView(this, viewProperty, viewDispatcher)
        return this
    }

    override fun mapViewIdToName(rulesHandler: RulesHandler) {
        id = View.generateViewId()
        rulesHandler.viewIdsMap[this.viewProperties.name] = id
    }

    override fun setOnDataPassListener(dataActionListener: DataActionListener) {
        if (this.dataActionListener == null) this.dataActionListener = dataActionListener
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
}
