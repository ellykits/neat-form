package com.nerdstone.neatformcore.robolectric.builders

import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.views.builders.EditTextViewBuilder
import com.nerdstone.neatformcore.views.controls.EditTextNFormView
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class `Test building EditText view` {

    private val viewProperty = spyk(NFormViewProperty())
    private val editTextNFormView = spyk(
        objToCopy = EditTextNFormView(RuntimeEnvironment.systemContext)
    )
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
    @Ignore("Throws stack overflow exception because of calling setPadding() method of super")
    fun `Should set padding on EditText `() {
        viewProperty.viewAttributes = hashMapOf("padding" to "12")
        editTextViewBuilder.buildView()
        Assert.assertTrue(editTextNFormView.paddingBottom == 12 && editTextNFormView.paddingTop == 12)
        Assert.assertTrue(editTextNFormView.paddingEnd == 12 && editTextNFormView.paddingStart == 12)
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}