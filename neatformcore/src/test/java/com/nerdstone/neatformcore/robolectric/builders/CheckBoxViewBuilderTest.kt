package com.nerdstone.neatformcore.robolectric.builders

import android.view.View
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
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
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class `Test building CheckBox view` {

    private val viewProperty = spyk(NFormViewProperty())
    private val checkBoxNFormView = CheckBoxNFormView(RuntimeEnvironment.systemContext)
    private val checkBoxViewBuilder = spyk(CheckBoxViewBuilder(checkBoxNFormView))

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "name"
        viewProperty.type = "checkbox"
        //Set EditText properties and assign EditText view builder
        checkBoxNFormView.viewProperties = viewProperty
    }

    @Test
    fun `Should set text on checkbox`() {
        val text = "Am a checkbox"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        checkBoxViewBuilder.buildView()
        Assert.assertTrue(checkBoxNFormView.text.isNotEmpty() && checkBoxNFormView.text.toString() == text)
        Assert.assertTrue(checkBoxNFormView.textSize.toInt() == 16)
    }

    @Test
    fun `Should set required on checkbox text `() {
        val text = "Am a required field"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.requiredStatus = "yes:Am required"
        checkBoxViewBuilder.buildView()
        Assert.assertTrue(
            checkBoxNFormView.text.toString().isNotEmpty() && checkBoxNFormView.text.toString().endsWith(
                "*"
            )
        )
    }

    @Test
    fun `Should uncheck the checkbox when visibility is gone`() {
        checkBoxNFormView.initView(viewProperty, spyk())
        checkBoxNFormView.isChecked = true
        checkBoxNFormView.visibility = View.GONE
        Assert.assertTrue(!checkBoxNFormView.isChecked)
    }

    @Test
    fun `Should set ViewDetails value to a map of name and label of the checkbox when state changes`() {
        checkBoxNFormView.initView(viewProperty, spyk())
        val text = "Am a checkbox"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        checkBoxViewBuilder.buildView()
        checkBoxNFormView.isChecked = true
        Assert.assertTrue(checkBoxNFormView.viewDetails.value != null)
        Assert.assertTrue((checkBoxNFormView.viewDetails.value as HashMap<*, *>).containsKey("name"))
        Assert.assertTrue((checkBoxNFormView.viewDetails.value as HashMap<*, *>)["name"]!! == text)
    }

    @Test
    fun `Should set ViewDetails value to a map of name and null when state changes`() {
        checkBoxNFormView.initView(viewProperty, spyk())
        val text = "Am a checkbox"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        checkBoxViewBuilder.buildView()
        checkBoxNFormView.isChecked = false
        Assert.assertTrue(checkBoxNFormView.viewDetails.value != null)
        Assert.assertTrue(
            (checkBoxNFormView.viewDetails.value as HashMap<String, String?>).containsKey("name")
        )
        Assert.assertTrue((checkBoxNFormView.viewDetails.value as HashMap<String, String?>)["name"] == null)
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}