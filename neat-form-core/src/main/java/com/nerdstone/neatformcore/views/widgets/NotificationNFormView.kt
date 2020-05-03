package com.nerdstone.neatformcore.views.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.nerdstone.neatformcore.domain.listeners.CalculationChangeListener
import com.nerdstone.neatformcore.domain.listeners.DataActionListener
import com.nerdstone.neatformcore.domain.listeners.VisibilityChangeListener
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.rules.NFormRulesHandler
import com.nerdstone.neatformcore.rules.NeatFormValidator
import com.nerdstone.neatformcore.views.builders.NotificationViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewVisibilityChangeHandler
import timber.log.Timber

class NotificationNFormView : FrameLayout, NFormView, CalculationChangeListener {

    override lateinit var viewProperties: NFormViewProperty
    override var dataActionListener: DataActionListener? = null
    override var visibilityChangeListener: VisibilityChangeListener? =
        ViewVisibilityChangeHandler.INSTANCE
    override val viewBuilder = NotificationViewBuilder(this)
    override val viewDetails = NFormViewDetails(this)
    override var formValidator: FormValidator = NeatFormValidator.INSTANCE

    private val rulesHandler = NFormRulesHandler.INSTANCE

    init {
        rulesHandler.calculationListeners.add(this)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun resetValueWhenHidden() = Unit

    override fun validateValue() = true

    override fun trackRequiredField() = Unit

    override fun onCalculationChanged(calculationField: Pair<String, Any?>) {
       Timber.i("Updated calculation ${calculationField.first} -> ${calculationField.second}")
        viewBuilder.updateNotificationText(calculationField)
    }
}