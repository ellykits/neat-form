package com.nerdstone.neatformcore.views.containers

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils

class VerticalRootView : LinearLayout, RootView {

    override lateinit var formBuilder: FormBuilder

    init {
        orientation = VERTICAL
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun initRootView(formBuilder: FormBuilder): RootView {
        this.formBuilder = formBuilder
        return this
    }

    override fun addChild(nFormView: NFormView) {
        val view = nFormView.viewDetails.view
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.bottomMargin = Utils.pxToDp(16f, context)
        params.topMargin = Utils.pxToDp(8f, context)
        params.marginStart = Utils.pxToDp(16f, context)
        params.marginEnd = Utils.pxToDp(16f, context)
        view.layoutParams = params
        this.addView(view)
    }

    override fun addChildren(
        viewProperties: List<NFormViewProperty>, formBuilder: FormBuilder ,buildFromLayout: Boolean
    ) = ViewUtils.createViews(this, viewProperties, formBuilder, buildFromLayout)
}
