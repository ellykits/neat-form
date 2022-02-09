package com.nerdstone.neatformcore.robolectric.builders

import android.text.InputType
import android.view.View
import com.nerdstone.neatformcore.domain.model.NFormFieldValidation
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.utils.setupView
import com.nerdstone.neatformcore.views.builders.MaskedEditTextViewBuilder
import com.nerdstone.neatformcore.views.widgets.MaskedEditTextNFormView
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MaskedEditTextViewBuilderTest : BaseJsonViewBuilderTest() {

    private val viewProperty = spyk(NFormViewProperty())
    private val maskedEditTextNFormView = MaskedEditTextNFormView(activity.get())
    private val maskedEditTextViewBuilder = spyk(MaskedEditTextViewBuilder(maskedEditTextNFormView))

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "name"
        viewProperty.type = "masked_edit_text"
        //Set MaskedEditText properties and assign MaskedEditText view builder
        maskedEditTextNFormView.viewProperties = viewProperty
        maskedEditTextNFormView.setupView(viewProperty, formBuilder)
    }

    @Test
    fun `Should set floating label`() {
        val hint = "Am a hint"
        viewProperty.viewAttributes = hashMapOf("hint" to hint)
        maskedEditTextViewBuilder.buildView()
        Assert.assertTrue(maskedEditTextNFormView.floatingLabelText.isNotEmpty() && maskedEditTextNFormView.floatingLabelText.toString() == hint)
    }

    @Test
    fun `Should set required on floating label `() {
        val hint = "Am a required field"
        viewProperty.viewAttributes = hashMapOf("hint" to hint)
        viewProperty.requiredStatus = "yes:Am required"
        maskedEditTextViewBuilder.buildView()
        Assert.assertTrue(
            maskedEditTextNFormView.floatingLabelText.isNotEmpty() && maskedEditTextNFormView.floatingLabelText.endsWith(
                "*"
            )
        )
    }

    @Test
    fun `Should set mask on the field `() {
        val mask = "##-##"
        viewProperty.viewAttributes = hashMapOf("mask" to mask)
        maskedEditTextViewBuilder.buildView()
        Assert.assertTrue(
            maskedEditTextNFormView.mask.toString().isNotEmpty() &&
                    maskedEditTextNFormView.mask.toString() == mask
        )
    }

    @Test
    fun `Should set mask hint on the field `() {
        val hint = "1234"
        viewProperty.viewAttributes = hashMapOf("mask_hint" to hint)
        maskedEditTextViewBuilder.buildView()
        Assert.assertTrue(
            maskedEditTextNFormView.hint.toString().isNotEmpty() &&
                    maskedEditTextNFormView.hint.toString() == hint
        )
    }

    @Test
    fun `Should set padding on MaskedEditText `() {
        viewProperty.viewAttributes = hashMapOf("padding" to "12")
        maskedEditTextViewBuilder.buildView()
        Assert.assertTrue(maskedEditTextNFormView.paddingBottom == 12 && maskedEditTextNFormView.paddingTop == 12)
        Assert.assertTrue(maskedEditTextNFormView.paddingEnd == 12 && maskedEditTextNFormView.paddingStart == 12)
    }

    @Test
    fun `Should reset the MaskedEditText value when visibility is gone`() {
       maskedEditTextNFormView.mask = "##-##"
        maskedEditTextNFormView.setValue("1122")
        maskedEditTextNFormView.visibility = View.GONE
        Assert.assertTrue(maskedEditTextNFormView.text.toString().isEmpty())
        Assert.assertTrue(maskedEditTextNFormView.mask.toString() == "##-##")
    }

    @Test
    fun `Should set Text to the MaskedEditText`() {
        //mask should be set before setting text
        maskedEditTextNFormView.mask = "##-##"
        maskedEditTextNFormView.setText("1122")
        maskedEditTextViewBuilder.buildView()
        Assert.assertFalse(maskedEditTextNFormView.text.toString().isEmpty())
    }

    @Test
    fun `Should validate MaskedEditText value`() {
        val validation = NFormFieldValidation()
        validation.condition =
            "value.matches(\"(\\\\d{2}-\\\\d{2}-\\\\d{4}-\\\\d{6})?\")"
        validation.message = "Please enter a valid CTC Number"

        viewProperty.validations = arrayListOf(validation)
        viewProperty.requiredStatus = "yes:Am required"
        maskedEditTextViewBuilder.buildView()
        maskedEditTextNFormView.mask = "##-##-####-######"
        maskedEditTextNFormView.setText("12345678912345")
        Assert.assertTrue(maskedEditTextNFormView.validateValue())


        maskedEditTextNFormView.setText("12345678912")
        Assert.assertFalse(maskedEditTextNFormView.validateValue())
        Assert.assertTrue(maskedEditTextNFormView.error == "Please enter a valid CTC Number")
    }

    @Test
    fun `Should check if mask is added to input`(){
        maskedEditTextNFormView.mask = "(###)-###-###"
        maskedEditTextNFormView.setText("012345678")
        maskedEditTextViewBuilder.buildView()
        Assert.assertTrue( maskedEditTextNFormView.text.toString()==("(012)-345-678"))
    }

    @Test
    fun `Should apply the provided input type`() {
        val hint = "Am a hint"
        viewProperty.viewAttributes = hashMapOf("mask" to "###", "hint" to hint, "input_type" to "phone")
        maskedEditTextViewBuilder.buildView()
        Assert.assertEquals(maskedEditTextNFormView.inputType, InputType.TYPE_CLASS_PHONE)
    }

    @Test
    fun `Should set value to the MaskedEditText when provided`() {
        maskedEditTextViewBuilder.buildView()
        val textValue = "0723721920"
        maskedEditTextNFormView.mask = "##########"
        maskedEditTextNFormView.setValue(textValue)
        Assert.assertEquals(maskedEditTextNFormView.initialValue, textValue)
        Assert.assertEquals(maskedEditTextNFormView.text.toString(), "0723721920")
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}