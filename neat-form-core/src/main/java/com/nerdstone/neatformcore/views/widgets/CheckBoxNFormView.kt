package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import com.nerdstone.neatformcore.domain.listeners.DataActionListener
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.ViewUtils.setReadOnlyState
import com.nerdstone.neatformcore.utils.removeAsterisk
import com.nerdstone.neatformcore.views.builders.CheckBoxViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewVisibilityChangeHandler

class CheckBoxNFormView : AppCompatCheckBox, NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override lateinit var formValidator: FormValidator
    override var dataActionListener: DataActionListener? = null
    override var visibilityChangeListener: VisibilityChangeListener? = ViewVisibilityChangeHandler
    override val viewBuilder = CheckBoxViewBuilder(this)
    override val viewDetails = NFormViewDetails(this)
    override var initialValue: Any? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        dataActionListener?.also {
            if (checked) {
                this.viewDetails.value =
                    mutableMapOf(viewDetails.name to text.toString().removeAsterisk())
            } else {
                this.viewDetails.value = null
            }
            it.onPassData(viewDetails)
        }
    }

    override fun resetValueWhenHidden() {
        isChecked = false
    }

    override fun trackRequiredField() = ViewUtils.handleRequiredStatus(this)

    override fun setValue(value: Any, enabled: Boolean) {
        initialValue = value
        if (value is Map<*, *>) isChecked = value.size == 1 && value.containsKey(viewDetails.name)
        setReadOnlyState(enabled)
    }

    override fun validateValue(): Boolean {
        val validationPair = formValidator.validateField(this)
        if (!validationPair.first) {
            this.error = validationPair.second
        } else this.error = null
        return validationPair.first
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        visibilityChangeListener?.onVisibilityChanged(this, visibility)
    }
}