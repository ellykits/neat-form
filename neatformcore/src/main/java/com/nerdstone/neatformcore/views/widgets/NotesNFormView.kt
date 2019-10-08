package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.domain.view.RulesHandler
import com.nerdstone.neatformcore.views.builders.EditTextViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class NotesNFormView : AppCompatTextView, NFormView {
    override fun initView(viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher): NFormView {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setOnDataPassListener(dataActionListener: DataActionListener) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapViewIdToName(rulesHandler: RulesHandler) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun resetValueWhenHidden() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override lateinit var viewProperties: NFormViewProperty
    override var dataActionListener: DataActionListener? = null
    override val viewBuilder: EditTextViewBuilder = EditTextViewBuilder(this)
    override var viewDetails: NFormViewDetails = NFormViewDetails(this)
    override val viewData get() = NFormViewData()
    override val nFormRootView get() = this.parent as RootView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
}