package com.nerdstone.neatformcore.views.controls

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.DataActionListener
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.form.json.NFormViewBuilder.makeView
import com.nerdstone.neatformcore.utils.Constants
import com.nerdstone.neatformcore.utils.NFormViewUtils.splitText
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

class EditTextNFormView : AppCompatEditText, NFormView {
    private var dataActionListener: DataActionListener? = null

    override var viewDetails: NFormViewDetails = NFormViewDetails()

    private var viewProperties: NFormViewProperty? = null

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

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setupView()
    }

    override fun initView(viewProperty: NFormViewProperty, viewDispatcher: ViewDispatcher): NFormView {
        this.viewDetails = NFormViewDetails(this)
        this.viewProperties = viewProperty

        viewDetails.name = viewProperty.name

        viewDetails.subjects = splitText(viewProperty.subjects, ",")

        if (viewProperty.viewAttributes != null) {
            makeView(viewProperty.viewAttributes!!, this, Constants.ViewType.EDIT_TEXT)
        }
        setOnDataPassListener(viewDispatcher)
        return this
    }

    override fun setOnDataPassListener(dataActionListener: DataActionListener) {
        if (this.dataActionListener == null) {
            this.dataActionListener = dataActionListener
        }
    }

    override fun setupView() {
        //TODO add implementation for setting up edit text
    }

    override fun onTextChanged(text: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)

        if (this.dataActionListener != null) {
            this.viewDetails.value = text
            this.dataActionListener!!.onPassData(viewDetails)
        }
    }
}
