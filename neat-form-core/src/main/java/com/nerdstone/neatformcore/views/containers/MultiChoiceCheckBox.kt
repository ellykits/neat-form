package com.nerdstone.neatformcore.views.containers

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.listeners.DataActionListener
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.handleRequiredStatus
import com.nerdstone.neatformcore.utils.pixelsToSp
import com.nerdstone.neatformcore.views.builders.MultiChoiceCheckBoxViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewVisibilityChangeHandler

class MultiChoiceCheckBox : LinearLayout, NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override lateinit var formValidator: FormValidator
    override var dataActionListener: DataActionListener? = null
    override var visibilityChangeListener: VisibilityChangeListener? =
        ViewVisibilityChangeHandler
    override val viewBuilder = MultiChoiceCheckBoxViewBuilder(this)
    override val viewDetails = NFormViewDetails(this)
    override var initialValue: Any? = null

    private var checkBoxOptionsTextSize: Float = 0f
    private var labelTextSize: Float = 0f

    init {
        orientation = VERTICAL
    }

    constructor(context: Context) : super(context)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setupViewAttributes(attrs)
    }

    override fun trackRequiredField() = handleRequiredStatus()

    override fun resetValueWhenHidden() {
        viewBuilder.resetCheckBoxValues()
    }

    override fun setValue(value: Any, enabled: Boolean) {
        initialValue = value
        if (value is Map<*, *>) {
            viewBuilder.setValue(value.keys, enabled)
        }
    }

    override fun validateValue() = formValidator.validateLabeledField(this)

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        visibilityChangeListener?.onVisibilityChanged(this, visibility)
    }

    /**
     * Obtain custom xml attributes passed for the stepper view
     */
    private fun setupViewAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.MultiChoiceCheckBox, 0, 0
            )
            try {
                checkBoxOptionsTextSize = context.pixelsToSp(
                    typedArray.getDimension(R.styleable.MultiChoiceCheckBox_options_text_size, 0f)
                )
                labelTextSize = context.pixelsToSp(
                    typedArray.getDimension(R.styleable.MultiChoiceCheckBox_label_text_size, 0f)
                )
            } finally {
                typedArray.recycle()
            }
        }
    }
}