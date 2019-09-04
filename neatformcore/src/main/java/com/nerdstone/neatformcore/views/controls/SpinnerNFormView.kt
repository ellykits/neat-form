package com.nerdstone.neatformcore.views.controls

import android.content.Context
import android.content.res.Resources
import android.support.v7.widget.AppCompatSpinner
import android.util.AttributeSet
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class SpinnerNFormView : AppCompatSpinner, NFormView {

    override var viewDetails: NFormViewDetails = NFormViewDetails(this)
    override lateinit var viewProperties: NFormViewProperty
    private var dataActionListener: DataActionListener? = null

    override val viewData: NFormViewData
        get() = NFormViewData()

    override val nFormRootView: RootView
        get() = this.parent as RootView

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, mode: Int) : super(context, mode) {
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, mode: Int) : super(context, attrs, defStyleAttr, mode) {
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, mode: Int,
                popupTheme: Resources.Theme) : super(context, attrs, defStyleAttr, mode, popupTheme) {
        setupView()
    }

    override fun initView(viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher): NFormView {
        setOnDataPassListener(viewDispatcher)
        return this
    }

    override fun setOnDataPassListener(dataActionListener: DataActionListener) {
        if (this.dataActionListener == null) {
            this.dataActionListener = dataActionListener
        }
    }

    override fun setupView() {

    }
}
