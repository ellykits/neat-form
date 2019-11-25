package com.nerdstone.neatformcore.domain.builders

import android.content.Context
import android.view.View
import com.nerdstone.neatformcore.domain.model.JsonFormStepBuilderModel
import com.nerdstone.neatformcore.rules.RulesFactory

interface FormBuilder {

    var fileSource: String

    val context: Context

    fun buildForm(
        jsonFormStepBuilderModel: JsonFormStepBuilderModel? = null,
        viewList: List<View>? = null
    ): FormBuilder

    fun createFormViews(
        context: Context,
        views: List<View>? = null,
        jsonFormStepBuilderModel: JsonFormStepBuilderModel? = null
    )

    fun registerFormRules(context: Context, rulesFileType: RulesFactory.RulesFileType)
}