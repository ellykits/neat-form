package com.nerdstone.neatformcore.domain.builders

import android.content.Context
import android.view.ViewGroup
import com.nerdstone.neatformcore.rules.RulesFactory

interface FormBuilder {

    var mainLayout: ViewGroup

    fun buildForm(source: String)

    fun createFormViews(context: Context)

    fun registerFormRules(context: Context, rulesFileType: RulesFactory.RulesFileType)
}