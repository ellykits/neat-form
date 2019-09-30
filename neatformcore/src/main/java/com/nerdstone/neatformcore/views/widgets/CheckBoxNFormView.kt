package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.domain.view.RulesHandler
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.CheckBoxViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class CheckBoxNFormView : CheckBox, NFormView {

    override var dataActionListener: DataActionListener? = null
    override val viewBuilder: ViewBuilder = CheckBoxViewBuilder(this)
    override val viewDetails: NFormViewDetails = NFormViewDetails(this)
    override lateinit var viewProperties: NFormViewProperty

    override val viewData: NFormViewData
        get() = NFormViewData()

    override val nFormRootView: RootView
        get() = this.parent as RootView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun initView(
        viewProperty: NFormViewProperty,
        viewDispatcher: ViewDispatcher
    ): NFormView = ViewUtils.setupView(this, viewProperty, viewBuilder, viewDispatcher)

    override fun setOnDataPassListener(dataActionListener: DataActionListener) {
        if (this.dataActionListener == null) this.dataActionListener = dataActionListener
    }


    override fun mapViewIdToName(rulesHandler: RulesHandler) {
        id = View.generateViewId()
        rulesHandler.viewIdsMap[this.viewProperties.name] = id
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        dataActionListener?.also {
            if (checked) {
                this.viewDetails.value = mutableMapOf(viewDetails.name to text.toString())
            } else {
                this.viewDetails.value = mutableMapOf(viewDetails.name to null)
            }
            it.onPassData(viewDetails)
        }

    }
}