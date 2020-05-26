package com.nerdstone.neatformcore.robolectric.builders

import android.view.View
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.CheckBoxViewBuilder
import com.nerdstone.neatformcore.views.widgets.CheckBoxNFormView
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
class CheckBoxViewBuilderTest : BaseJsonViewBuilderTest() {

    private val viewProperty = spyk(NFormViewProperty())
    private val checkBoxNFormView = CheckBoxNFormView(activity.get())
    private val checkBoxViewBuilder = spyk(CheckBoxViewBuilder(checkBoxNFormView))

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "name"
        viewProperty.type = "checkbox"
        //Set EditText properties and assign EditText view builder
        checkBoxNFormView.formValidator = this.formValidator
        checkBoxNFormView.viewProperties = viewProperty
        ViewUtils.setupView(checkBoxNFormView, viewProperty, spyk())
    }

    @Test
    fun `Should set text and textSize on checkbox`() {
        val text = "Am a checkbox"
        val checkBoxTextSize = 14
        viewProperty.viewAttributes = mutableMapOf("text" to text, "text_size" to checkBoxTextSize)
        checkBoxViewBuilder.buildView()
        Assert.assertTrue(checkBoxNFormView.text.isNotEmpty() && checkBoxNFormView.text.toString() == text)
        Assert.assertTrue(checkBoxNFormView.textSize.toInt() == checkBoxTextSize)
    }

    @Test
    fun `Should set required on checkbox text `() {
        val text = "Am a required field"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.requiredStatus = "yes:Am required"
        checkBoxViewBuilder.buildView()
        Assert.assertTrue(
            checkBoxNFormView.text.toString().isNotEmpty() && checkBoxNFormView.text.toString()
                .endsWith(
                    "*"
                )
        )
    }

    @Test
    fun `Should uncheck the checkbox when visibility is gone`() {
        checkBoxViewBuilder.buildView()
        checkBoxNFormView.isChecked = true
        checkBoxNFormView.visibility = View.GONE
        Assert.assertFalse(checkBoxNFormView.isChecked)
    }

    @Test
    fun `Should set ViewDetails value to a map of name and label of the checkbox when state changes`() {
        val text = "Am a checkbox"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        checkBoxViewBuilder.buildView()
        checkBoxNFormView.isChecked = true
        Assert.assertTrue(checkBoxNFormView.viewDetails.value != null)
        Assert.assertTrue((checkBoxNFormView.viewDetails.value as HashMap<*, *>).containsKey("name"))
        Assert.assertTrue((checkBoxNFormView.viewDetails.value as HashMap<*, *>)["name"]!! == text)
    }

    @Test
    fun `Should set ViewDetails value to null when unchecked`() {
        val text = "Am a checkbox"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        checkBoxViewBuilder.buildView()
        checkBoxNFormView.isChecked = false
        Assert.assertNull(checkBoxNFormView.viewDetails.value)
    }

    @Test
    fun `Should set value when provided`() {
        checkBoxViewBuilder.buildView()
        val valueHashMap = mapOf("name" to NFormViewData().apply { value = "Am a checkbox" })
        checkBoxNFormView.setValue(valueHashMap)
        Assert.assertEquals(checkBoxNFormView.initialValue, valueHashMap)
        Assert.assertTrue(checkBoxNFormView.isChecked)
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}