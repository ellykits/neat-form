package com.nerdstone.neatformcore.robolectric.builders

import android.view.View
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormFieldValidation
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.views.builders.EditTextViewBuilder
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.lang.reflect.Type


@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class `Test building EditText view` {

    private val viewProperty = spyk(NFormViewProperty())
    private val editTextNFormView =  EditTextNFormView(RuntimeEnvironment.systemContext)
    private val editTextViewBuilder = spyk(EditTextViewBuilder(editTextNFormView))

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "name"
        viewProperty.type = "edit_text"
        //Set EditText properties and assign EditText view builder
        editTextNFormView.viewProperties = viewProperty
    }

    @Test
    fun `Should set hint and text size`() {
        val hint = "Am a hint"
        viewProperty.viewAttributes = hashMapOf("hint" to hint, "text_size" to "12")
        editTextViewBuilder.buildView()
        Assert.assertTrue(editTextNFormView.hint.isNotEmpty() && editTextNFormView.hint.toString() == hint)
        Assert.assertTrue(editTextNFormView.textSize.toInt() == 12)
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
        editTextNFormView.visibility = View.GONE
        Assert.assertTrue(editTextNFormView.text.toString().isEmpty())
    }

    @Test
    fun `Should validate EditText `() {
        val validations = "[{\"validation_name\":\"length\",\"condition\":\"value.length() < 8\",\"error_message\":\"value should be less than eight digits\"},{\"validation_name\":\"special characters\",\"condition\":\"!value.contains('@')\",\"error_message\":\"value should not contain special character\"}]"
        val listType: Type =
            object : TypeToken<List<NFormFieldValidation?>?>() {}.type
        viewProperty.validations =Gson().fromJson(validations,listType)
        viewProperty.requiredStatus = "yes:Am required"
        editTextViewBuilder.buildView()
        editTextNFormView.setText("yes")

        Assert.assertTrue(editTextNFormView.validateValue())


        editTextNFormView.setText("1234567890")
        Assert.assertFalse(editTextNFormView.validateValue())
        Assert.assertTrue(editTextNFormView.error.toString().isNotEmpty() &&
                editTextNFormView.error.toString() == "value should be less than eight digits")

        editTextNFormView.setText("w@gmail")
        Assert.assertFalse(editTextNFormView.validateValue())
        Assert.assertTrue(editTextNFormView.error.toString().isNotEmpty() &&
                editTextNFormView.error.toString() == "value should not contain special character")

    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}