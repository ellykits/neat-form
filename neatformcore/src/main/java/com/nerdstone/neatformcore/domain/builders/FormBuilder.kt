package com.nerdstone.neatformcore.domain.builders

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.nerdstone.neatformcore.rules.RulesFactory

interface FormBuilder {

    var fileSource: String

    var mainLayout: ViewGroup

    fun buildForm(view: View?): FormBuilder

    fun createFormViews(context: Context)

    fun registerFormRules(context: Context, rulesFileType: RulesFactory.RulesFileType)
}