package com.nerdstone.neatformcore.robolectric.builders

import android.text.InputType
import android.view.View
import com.nerdstone.neatformcore.domain.model.NFormFieldValidation
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.TextInputEditTextBuilder
import com.nerdstone.neatformcore.views.widgets.TextInputEditTextNFormView
import io.mockk.spyk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by cozej4 on 2019-11-20.
 *
 * @cozej4 https://github.com/cozej4
 */

class TextInputEditTextBuilderTest : BaseJsonViewBuilderTest(){

    private val viewProperty = spyk(NFormViewProperty())
    private val textInputEditTextNFormView = TextInputEditTextNFormView(activity.get())
    private val testInputLayoutBuilder = spyk(
        objToCopy = TextInputEditTextBuilder(textInputEditTextNFormView),
        recordPrivateCalls = true
    )

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "first_name"
        viewProperty.type = "text_input_layout"
        textInputEditTextNFormView.viewProperties = viewProperty
        ViewUtils.setupView(textInputEditTextNFormView, viewProperty, formBuilder)
    }

    @Test
    fun `Should set hint on the Layout Inflater`() {
        val hint = "Am a hint"
        viewProperty.viewAttributes = hashMapOf("hint" to hint, "text_size" to "12")
        testInputLayoutBuilder.buildView()
        Assert.assertTrue(textInputEditTextNFormView.hint.toString() == hint)
        Assert.assertTrue(textInputEditTextNFormView.editText?.textSize?.toInt() == 12)
    }

    @Test
    fun `Should set required on hint `() {
        val hint = "Am a required field"
        viewProperty.viewAttributes = hashMapOf("hint" to hint)
        viewProperty.requiredStatus = "yes:Am required"
        testInputLayoutBuilder.buildView()
        Assert.assertTrue(textInputEditTextNFormView.hint!!.endsWith("*"))
    }


    @Test
    fun `Should reset the TextInputEditTet value when visibility is gone`() {
        textInputEditTextNFormView.visibility = View.VISIBLE
        textInputEditTextNFormView.editText?.setText("Some text")
        textInputEditTextNFormView.visibility = View.GONE
        Assert.assertTrue(textInputEditTextNFormView.editText?.text.toString().isEmpty())
    }

    @Test
    fun `Should validate TextInputEditText value`() {
        val validation = NFormFieldValidation()
        validation.condition =
            " value.matches(\"^[\\\\w-_\\\\.+]*[\\\\w-_\\\\.]\\\\@([\\\\w]+\\\\.)+[\\\\w]+[\\\\w]\$\")"
        validation.message = "Please enter a valid email address"

        viewProperty.validations = arrayListOf(validation)
        viewProperty.requiredStatus = "yes:Am required"
        testInputLayoutBuilder.buildView()
        textInputEditTextNFormView.editText?.setText("johndoe@gmail.com")
        Assert.assertTrue(textInputEditTextNFormView.validateValue())

        textInputEditTextNFormView.editText?.setText("johndoegmail.com")
        Assert.assertFalse(textInputEditTextNFormView.validateValue())
        Assert.assertTrue(textInputEditTextNFormView.error == "Please enter a valid email address")
    }


    @Test
    fun `Should set value to the edittext when provided`() {
        testInputLayoutBuilder.buildView()
        val textValue = "0723721920"
        textInputEditTextNFormView.setValue(textValue)
        Assert.assertEquals(textInputEditTextNFormView.initialValue, textValue)
        Assert.assertEquals(textInputEditTextNFormView.editText?.text.toString(), "0723721920")
    }

    @Test
    fun `Should apply the provided input type`(){
        val hint = "Am a hint"
        viewProperty.viewAttributes = hashMapOf("hint" to hint, "input_type" to "number")
        testInputLayoutBuilder.buildView()
        Assert.assertEquals(textInputEditTextNFormView.editText?.inputType , InputType.TYPE_CLASS_NUMBER )
    }
}