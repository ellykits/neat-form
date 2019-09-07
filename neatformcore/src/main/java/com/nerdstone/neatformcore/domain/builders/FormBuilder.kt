package com.nerdstone.neatformcore.domain.builders

import android.content.Context
import android.view.ViewGroup
import com.nerdstone.neatformcore.domain.model.NForm
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher

interface FormBuilder {

    var mainLayout: ViewGroup

    fun getForm(source: String): NForm?

    fun createFormViews(context: Context)

    fun setViewDispatcher(viewDispatcher: ViewDispatcher)

    fun registerFormRules(context: Context, rulesFileType: RulesFactory.RulesFileType)

    fun freeResources()
}