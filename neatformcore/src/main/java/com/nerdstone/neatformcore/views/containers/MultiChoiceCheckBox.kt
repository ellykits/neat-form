package com.nerdstone.neatformcore.views.containers

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.domain.view.RulesHandler
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.MultiChoiceCheckBoxViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class MultiChoiceCheckBox : LinearLayout, NFormView {

    override val viewBuilder: ViewBuilder = MultiChoiceCheckBoxViewBuilder(this)
    override var dataActionListener: DataActionListener? = null
    override val viewDetails: NFormViewDetails
        get() = NFormViewDetails(this)

    init {
        orientation = VERTICAL
    }

    override val viewData: NFormViewData
        get() = NFormViewData()

    override val nFormRootView: RootView
        get() = this.parent as RootView

    override lateinit var viewProperties: NFormViewProperty

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

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
}