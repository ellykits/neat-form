package com.nerdstone.neatformcore.domain.builders

import android.content.Context
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.view.FormValidator
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.viewmodel.DataViewModel
import com.nerdstone.neatformcore.viewmodel.FormViewModel
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import kotlin.reflect.KClass

/**
 * [FormBuilder] contract is implemented by any builder using any preferred Schema.
 *
 * [formString] is the string representation of the form depending on the schema which can be JSON
 * YAML or even XML
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

    var dataViewModel: DataViewModel

    val formValidator: FormValidator

    var registeredViews: HashMap<String, KClass<*>>

    var formViewModel: FormViewModel

    val rulesFactory: RulesFactory

    val viewDispatcher: ViewDispatcher

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
     * Call this method before building the form to supply the fields with data obtained from [formDataJson]. You
     * can optionally pass [readOnlyFields] which will be disabled.
     */
    fun withFormData(
        formDataJson: String, readOnlyFields: MutableSet<String> = mutableSetOf()
    ): FormBuilder

    /**
     * This method is called right after the views have been created. The data provided will be delegated to
     * the view model which will call the method for setting values on its observers (fields)
     */
    fun preFillForm()
}