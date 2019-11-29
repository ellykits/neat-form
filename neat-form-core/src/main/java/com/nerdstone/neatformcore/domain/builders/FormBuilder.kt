package com.nerdstone.neatformcore.domain.builders

import android.content.Context
import android.view.View
import com.nerdstone.neatandroidstepper.core.widget.NeatStepperLayout
import com.nerdstone.neatformcore.domain.model.JsonFormStepBuilderModel
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.rules.RulesFactory

interface FormBuilder {

    var fileSource: String

    val context: Context

    var neatStepperLayout: NeatStepperLayout

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

    fun getFormDetails():HashMap<String, NFormViewData>

    fun getFormMetaData(): Map<String, Any>
}