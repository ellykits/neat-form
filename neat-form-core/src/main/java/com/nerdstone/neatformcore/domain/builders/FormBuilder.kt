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

/**
 * [FormBuilder] contract is implemented by any builder using any preferred Schema.
 *
 * [formString] is the string representation of the form depending on the schema which can be JSON
 * YAML or even XML
 *
 * The form builder requires a FragmentActivity [context] which it will use to create its views. If the form is
 * generated with a stepper. That is when the generated form is not embedded into an existing view. The
 * [neatStepperLayout] is the view holding the form steps.
 *
 * The form builder also uses the [formValidator] to validate fields.
 *
 * Before building views, views are first registered withing the [registeredViews] map. This is a map
 * that holds the view type against the actual class type of the view that will used to create the view for example
 *              for an EditText inside the view type map you will have
 *              ["edit_text" -> EditTextNFormView::class]
 *
 * This map can be overridden if you have custom views that you also want rendered. Just remember to register the views
 * before building the form.
 *
 */
interface FormBuilder {

    var formString: String?

    val context: Context

    var neatStepperLayout: NeatStepperLayout

    var viewModel: DataViewModel

    var formValidator: FormValidator

    var registeredViews: HashMap<String, KClass<*>>

    /**
     * THis is the method that hooks up everything on the form builder. It parses the file say JSON file that it has
     * been provided with, reads the rules from Assets directory and generate the actual views. The functionality
     * for generating views is delegated to [createFormViews]
     */
    fun buildForm(
            jsonFormStepBuilderModel: JsonFormStepBuilderModel? = null, viewList: List<View>? = null
    ): FormBuilder

    /**
     * This method creates within [context]. It also has an optional [jsonFormStepBuilderModel] parameter
     * that can be used to customize the stepper that will be shown on the [neatStepperLayout]
     *
     * When using custom layout to build the form you need to provide the layouts to the [views] list.
     * The form builder will generate the views for steps in the order that the layouts are added to the list
     * i.e. for step one views will be on the layout in position 0 of the [views]
     *
     */
    fun createFormViews(
            context: Context, views: List<View>? = null,
            jsonFormStepBuilderModel: JsonFormStepBuilderModel? = null
    )

    /**
     * This method reads the rules file available withing the given [context] and returns true when done
     * false otherwise. You also setup the [rulesFileType] in order for this to work. The form builder
     * uses Rules engine that allows you to write rules either in JSON or YML format.
     */
    fun registerFormRulesFromFile(
            context: Context, rulesFileType: RulesFactory.RulesFileType
    ): Boolean

    /**
     * This method returns a map of the form data. Where the key is the field name and the value is of
     * type [NFormViewData]
     */
    fun getFormData(): HashMap<String, NFormViewData>

    /**
     * This method returns Form metadata as specified on the form
     */
    fun getFormMetaData(): Map<String, Any>

    /**
     * This method returns a JSON string representation of the form data including the metadata specified on the
     * form.
     */
    fun getFormDataAsJson(): String

    /**
     * Use this method to add view types to the [registeredViews] property
     */
    fun registerViews()

    /**
     * Use this method to update the view model which will in turn update its observers (views)
     * This method expect [formDataJson] to comply with the json string that was returned by the form builder.
     * The string will be parsed into a map of [NFormViewData] with the key being the name of the field
     * then use the new map as the data for the view model.
     *
     * Any field that exists in the list of [readOnlyFields] will be disabled.
     */

    fun updateFormData(formDataJson: String, readOnlyFields: MutableSet<String> = mutableSetOf())

}