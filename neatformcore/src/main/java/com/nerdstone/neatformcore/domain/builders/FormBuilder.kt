package com.nerdstone.neatformcore.domain.builders

import android.content.Context
import android.view.ViewGroup
import com.nerdstone.neatformcore.rules.RulesFactory

interface FormBuilder {

    var fileSource: String

    var mainLayout: ViewGroup

    fun buildForm() :FormBuilder

    fun createFormViews(context: Context)

    fun registerFormRules(context: Context, rulesFileType: RulesFactory.RulesFileType)
}