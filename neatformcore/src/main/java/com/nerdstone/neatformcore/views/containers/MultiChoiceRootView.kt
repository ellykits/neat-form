package com.nerdstone.neatformcore.views.containers

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.views.controls.CheckboxNFormView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class MultiChoiceRootView : LinearLayout, RootView {

    private var label: String? = null
    private var labelTextView: TextView? = null

    override val viewsData: List<NFormViewData> = listOf()

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        LayoutInflater.from(context).inflate(
            R.layout.multi_choice_nformview_layout,
            this, true
        )
        setupAttributes(attrs)
        setupView()
    }

    override fun initRootView(): RootView {
        setupView()
        return this
    }

    override fun addChild(nFormView: NFormView) {
        addView(nFormView.viewDetails.view)
    }

    override fun addChildren(
        viewProperties: List<NFormViewProperty>,
        viewDispatcher: ViewDispatcher
    ) {
        for (count in 0..4) {
            addChild(CheckboxNFormView(context))
        }
    }

    private fun setupAttributes(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CheckboxNFormView, 0, 0
        )
        try {
            label = typedArray.getString(R.styleable.CheckboxNFormView_multiChoiceLabel)
        } finally {
            typedArray.recycle()
        }
    }

    private fun createLabel() {
        labelTextView = findViewById(R.id.multichoiceLabel)
        labelTextView!!.text = if (label != null) label else ""
        labelTextView!!.textSize = 18f
        labelTextView!!.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        labelTextView!!.setTextColor(context.resources.getColor(R.color.colorBlack))
    }

    override fun setupView() {
        orientation = VERTICAL
        if (layoutParams != null) {
            layoutParams.height = LayoutParams.WRAP_CONTENT
            layoutParams.width = LayoutParams.MATCH_PARENT
        }
        createLabel()
    }
}
