package com.nerdstone.neatformcore.domain.builders

import android.content.Context
import android.view.View
import com.nerdstone.neatandroidstepper.core.widget.NeatStepperLayout
import com.nerdstone.neatformcore.domain.model.JsonFormStepBuilderModel
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.viewmodel.DataViewModel
import kotlin.reflect.KClass

interface FormBuilder {

    var jsonString: String?

    val context: Context

    var neatStepperLayout: NeatStepperLayout

    var viewModel: DataViewModel

    var formValidator: FormValidator

    var registeredViews: HashMap<String, KClass<*>>

    fun buildForm(
            jsonFormStepBuilderModel: JsonFormStepBuilderModel? = null, viewList: List<View>? = null
    ): FormBuilder

    fun createFormViews(
            context: Context, views: List<View>? = null,
            jsonFormStepBuilderModel: JsonFormStepBuilderModel? = null
    )

    fun registerFormRulesFromFile(
            context: Context, rulesFileType: RulesFactory.RulesFileType
    ): Boolean

    fun getFormData(): HashMap<String, NFormViewData>

    fun getFormMetaData(): Map<String, Any>

    fun getFormDataAsJson(): String

    fun registerViews()

}