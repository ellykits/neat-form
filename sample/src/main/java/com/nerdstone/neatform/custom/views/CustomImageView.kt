package com.nerdstone.neatform.custom.views

import android.content.Context
import com.nerdstone.neatform.custom.builders.CustomImageViewBuilder
import com.nerdstone.neatformcore.domain.listeners.DataActionListener
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.rules.NeatFormValidator
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.handlers.ViewVisibilityChangeHandler
import de.hdodenhof.circleimageview.CircleImageView

class CustomImageView(context: Context) : CircleImageView(context), NFormView {

    override lateinit var viewProperties: NFormViewProperty
    override lateinit var formValidator: FormValidator
    override var dataActionListener: DataActionListener? = null
    override var visibilityChangeListener: VisibilityChangeListener? =
            ViewVisibilityChangeHandler.INSTANCE
    override val viewBuilder = CustomImageViewBuilder(this)
    override var viewDetails = NFormViewDetails(this)
    override var initialValue: Any? = null

    override fun resetValueWhenHidden() = Unit

    override fun validateValue(): Boolean = true

    override fun trackRequiredField() = ViewUtils.handleRequiredStatus(this)

    override fun setValue(value: Any, enabled: Boolean) {
        TODO("Not yet implemented")
    }
}