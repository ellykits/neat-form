package com.nerdstone.neatformcore.domain.builders

import android.content.Context
import android.view.ViewGroup
import com.nerdstone.neatformcore.domain.model.NForm
import com.nerdstone.neatformcore.rules.RulesFactory

interface FormBuilder {

    var mainLayout: ViewGroup

    fun getForm(source: String): NForm?

    fun createFormViews(context: Context)

    fun registerFormRules(context: Context, rulesFileType: RulesFactory.RulesFileType)

    fun freeResources()
}