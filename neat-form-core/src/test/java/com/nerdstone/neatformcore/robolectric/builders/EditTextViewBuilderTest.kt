package com.nerdstone.neatformcore.robolectric.builders

import android.text.InputType
import android.view.View
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormFieldValidation
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.EditTextViewBuilder
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class EditTextViewBuilderTest : BaseJsonViewBuilderTest() {

    private val viewProperty = spyk(NFormViewProperty())
    private val editTextNFormView = EditTextNFormView(activity.get())
    private val editTextViewBuilder = spyk(EditTextViewBuilder(editTextNFormView))
    private val dataActionListener = spyk(objToCopy = ViewDispatcher.INSTANCE)

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "name"
        viewProperty.type = "edit_text"
        //Set EditText properties and assign EditText view builder
        editTextNFormView.formValidator = this.formValidator
        editTextNFormView.viewProperties = viewProperty
        ViewUtils.setupView(editTextNFormView, viewProperty, dataActionListener)
    }

    @Test
    fun `Should set hint and text size`() {
        val hint = "Am a hint"
        viewProperty.viewAttributes = hashMapOf("hint" to hint, "text_size" to "12")
        editTextViewBuilder.buildView()
        Assert.assertTrue(editTextNFormView.hint.isNotEmpty() && editTextNFormView.hint.toString() == hint)
        Assert.assertTrue(editTextNFormView.textSize.toInt() == 18)
    }

    @Test
    fun `Should set required on hint `() {
        val hint = "Am a required field"
        viewProperty.viewAttributes = hashMapOf("hint" to hint)
        viewProperty.requiredStatus = "yes:Am required"
        editTextViewBuilder.buildView()
        Assert.assertTrue(editTextNFormView.hint.isNotEmpty() && editTextNFormView.hint.endsWith("*"))
    }

    @Test
    fun `Should set text on the field `() {
        val text = "Am a sample text on field"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        editTextViewBuilder.buildView()
        Assert.assertTrue(editTextNFormView.text.toString().isNotEmpty() &&
                editTextNFormView.text.toString() == "Am a sample text on field")
    }

    @Test
    fun `Should set padding on EditText `() {
        viewProperty.viewAttributes = hashMapOf("padding" to "12")
        editTextViewBuilder.buildView()
        Assert.assertTrue(editTextNFormView.paddingBottom == 12 && editTextNFormView.paddingTop == 12)
        Assert.assertTrue(editTextNFormView.paddingEnd == 12 && editTextNFormView.paddingStart == 12)
    }

    @Test
    fun `Should reset the EditText value when visibility is gone`() {
        editTextNFormView.setText("Some text")
        editTextNFormView.visibility = View.GONE
        Assert.assertTrue(editTextNFormView.text.toString().isEmpty())
    }

    @Test
    fun `Should validate EditText value`() {
        val validation = NFormFieldValidation()
        validation.condition = " value.matches(\"^[\\\\w-_\\\\.+]*[\\\\w-_\\\\.]\\\\@([\\\\w]+\\\\.)+[\\\\w]+[\\\\w]\$\")"
        validation.message = "Please enter a valid email address"

        viewProperty.validations = arrayListOf(validation)
        viewProperty.requiredStatus = "yes:Am required"
        editTextViewBuilder.buildView()
        editTextNFormView.setText("johndoe@gmail.com")
        Assert.assertTrue(editTextNFormView.validateValue())


        editTextNFormView.setText("johndoegmail.com")
        Assert.assertFalse(editTextNFormView.validateValue())
        Assert.assertTrue(editTextNFormView.error == "Please enter a valid email address")
    }

    @Test
    fun `Should apply the provided input type`(){
        val hint = "Am a hint"
        viewProperty.viewAttributes = hashMapOf("hint" to hint, "input_type" to "phone")
        editTextViewBuilder.buildView()
        Assert.assertEquals(editTextNFormView.inputType , InputType.TYPE_CLASS_PHONE )
    }

    @Test
    fun `Should set value to the edittext when provided`() {
        editTextViewBuilder.buildView()
        val textValue = "0723721920"
        editTextNFormView.setValue(textValue)
        Assert.assertEquals(editTextNFormView.initialValue, textValue)
        Assert.assertEquals(editTextNFormView.text.toString(), "0723721920")
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}