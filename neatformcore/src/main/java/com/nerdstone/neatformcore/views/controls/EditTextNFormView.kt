package com.nerdstone.neatformcore.views.controls

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import com.nerdstone.neatformcore.domain.data.DataActionListener
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.EditTextViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class EditTextNFormView : AppCompatEditText, NFormView {
    private var dataActionListener: DataActionListener? = null

    override var viewDetails: NFormViewDetails = NFormViewDetails(this)

    private val editTextBuilder: EditTextViewBuilder =
        EditTextViewBuilder(this)

    override lateinit var viewProperties: NFormViewProperty

    override val viewData: NFormViewData
        get() = NFormViewData()

    override val nFormRootView: RootView
        get() = this.parent as RootView

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        setupView()
    }

    override fun initView(viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher)
            : NFormView {

        this.viewProperties = viewProperty
        viewDetails.name = viewProperty.name
        viewDetails.subjects = ViewUtils.splitText(viewProperty.subjects, ",")
        setOnDataPassListener(viewDispatcher)
        editTextBuilder.buildView()
        return this
    }

    override fun setOnDataPassListener(dataActionListener: DataActionListener) {
        if (this.dataActionListener == null) {
            this.dataActionListener = dataActionListener
        }
    }

    override fun setupView() {
        //overridden
    }

    override fun onTextChanged(
        text: CharSequence, start: Int, lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        dataActionListener?.also {
            this.viewDetails.value = text
            it.onPassData(viewDetails)
        }
    }
}
