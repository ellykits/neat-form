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
import com.nerdstone.neatformcore.rules.NeatFormValidator
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.MultiChoiceCheckBoxViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.handlers.ViewVisibilityChangeHandler
import timber.log.Timber

class MultiChoiceCheckBox : LinearLayout, NFormView {
    override lateinit var viewProperties: NFormViewProperty
    override var dataActionListener: DataActionListener? = null
    override var visibilityChangeListener: VisibilityChangeListener? =
        ViewVisibilityChangeHandler.INSTANCE
    override val viewBuilder = MultiChoiceCheckBoxViewBuilder(this)
    override val viewDetails = NFormViewDetails(this)
    override var formValidator: FormValidator = NeatFormValidator.INSTANCE

    //Attribute value passed from the view's AttributeSet
    private var checkBoxOptionsTextSize: Float = 0f
    private var labelTextSize: Float = 0f

    init {
        orientation = VERTICAL
    }

    constructor(context: Context) : super(context)


    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setupViewAttributes(attrs)
    }

    override fun initView(
        viewProperty: NFormViewProperty,
        viewDispatcher: ViewDispatcher
    ): NFormView {
        setPassedAttributes(viewProperty)
        ViewUtils.setupView(this, viewProperty, viewDispatcher)
        return this
    }

    override fun trackRequiredField() = ViewUtils.handleRequiredStatus(this)

    override fun resetValueWhenHidden() {
        viewBuilder.resetCheckBoxValues()
    }

    override fun validateValue(): Boolean =
        formValidator.validateLabeledField(this)

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
                checkBoxOptionsTextSize = Utils.pixelsToSp(
                    context, typedArray.getDimension(
                        R.styleable.MultiChoiceCheckBox_checkbox_options_text_size,
                        0f
                    )
                )


                labelTextSize = Utils.pixelsToSp(
                    context, typedArray.getDimension(
                        R.styleable.MultiChoiceCheckBox_label_text_size,
                        0f
                    )
                )


                Timber.i("obtained checkbox options font size size = %s ", checkBoxOptionsTextSize)
            } finally {
                typedArray.recycle()
            }
        }
    }

    /**
     * adding passed xml attributes to MultiChoice Checkbox viewAttributes
     */
    private fun setPassedAttributes(viewProperty: NFormViewProperty) {
        if (checkBoxOptionsTextSize != 0f) {
            viewProperty.viewAttributes?.put(
                MultiChoiceCheckBoxViewBuilder.MultiChoiceCheckBoxProperties.CHECK_BOX_OPTIONS_TEXT_SIZE.name,
                checkBoxOptionsTextSize
            )
        }

        if (labelTextSize != 0f) {
            viewProperty.viewAttributes?.put(
                MultiChoiceCheckBoxViewBuilder.MultiChoiceCheckBoxProperties.LABEL_TEXT_SIZE.name,
                labelTextSize
            )
        }
    }
}