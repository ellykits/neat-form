package com.nerdstone.neatformcore.robolectric.builders

import android.view.View
import androidx.test.core.app.ApplicationProvider
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormFieldValidation
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.views.builders.TextInputEditTextBuilder
import com.nerdstone.neatformcore.views.widgets.TextInputEditTextNFormView
import io.mockk.spyk
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by cozej4 on 2019-11-20.
 *
 * @cozej4 https://github.com/cozej4
 */
@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class `Text building InputLayout View` {

    private val viewProperty = spyk(NFormViewProperty())
    private val textInputLayoutNFormView =
            TextInputEditTextNFormView(ApplicationProvider.getApplicationContext())
    private val testInputLayoutBuilder = spyk(
            objToCopy = TextInputEditTextBuilder(textInputLayoutNFormView),
            recordPrivateCalls = true
    )

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "first_name"
        viewProperty.type = "text_input_layout"
        textInputLayoutNFormView.viewProperties = viewProperty
    }

    @Test
    fun `Should set hint on the Layout Inflater`() {
        val hint = "Am a hint"
        viewProperty.viewAttributes = hashMapOf("hint" to hint, "text_size" to "12")
        testInputLayoutBuilder.buildView()
        Assert.assertTrue(textInputLayoutNFormView.hint.toString() == hint)
        Assert.assertTrue(textInputLayoutNFormView.editText?.textSize?.toInt() == 12)
    }

    @Test
    fun `Should set required on hint `() {
        val hint = "Am a required field"
        viewProperty.viewAttributes = hashMapOf("hint" to hint)
        viewProperty.requiredStatus = "yes:Am required"
        testInputLayoutBuilder.buildView()
        Assert.assertTrue(textInputLayoutNFormView.hint!!.endsWith("*"))
    }


    @Test
    fun `Should reset the TextInputEditTet value when visibility is gone`() {
        textInputLayoutNFormView.visibility = View.GONE
        Assert.assertTrue(textInputLayoutNFormView.editText?.text==null)
    }

    @Test
    fun `Should validate TextInputEditText value`() {
        val validation = NFormFieldValidation()
        validation.condition = " value.matches(\"^[\\\\w-_\\\\.+]*[\\\\w-_\\\\.]\\\\@([\\\\w]+\\\\.)+[\\\\w]+[\\\\w]\$\")"
        validation.message = "Please enter a valid email address"

        viewProperty.validations = arrayListOf(validation)
        viewProperty.requiredStatus = "yes:Am required"
        testInputLayoutBuilder.buildView()
        textInputLayoutNFormView.editText?.setText("johndoe@gmail.com")
        Assert.assertTrue(textInputLayoutNFormView.validateValue())

        textInputLayoutNFormView.editText?.setText("johndoegmail.com")
        Assert.assertFalse(textInputLayoutNFormView.validateValue())
        Assert.assertTrue(textInputLayoutNFormView.error=="Please enter a valid email address")
    }
}