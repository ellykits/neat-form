package com.nerdstone.neatformcore.views.containers

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.createViews
import com.nerdstone.neatformcore.utils.pxToDp

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
        params.bottomMargin = context.pxToDp(16f)
        params.topMargin = context.pxToDp(8f)
        params.marginStart = context.pxToDp(16f)
        params.marginEnd = context.pxToDp(16f)
        view.layoutParams = params
        this.addView(view)
    }

    override fun addChildren(
        viewProperties: List<NFormViewProperty>, formBuilder: FormBuilder, buildFromLayout: Boolean
    ) = createViews(viewProperties, formBuilder, buildFromLayout)
}
