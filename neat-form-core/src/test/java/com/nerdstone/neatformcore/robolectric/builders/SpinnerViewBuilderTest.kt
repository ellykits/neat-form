package com.nerdstone.neatformcore.robolectric.builders

import android.view.View
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.nerdstone.neatformcore.domain.model.NFormSubViewProperty
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.utils.setupView
import com.nerdstone.neatformcore.views.builders.SpinnerViewBuilder
import com.nerdstone.neatformcore.views.widgets.SpinnerNFormView
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SpinnerViewBuilderTest : BaseJsonViewBuilderTest() {

    private val viewProperty = spyk(NFormViewProperty())
    private val spinnerNFormView = SpinnerNFormView(activity.get())
    private lateinit var spinnerViewBuilder: SpinnerViewBuilder

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "gender"
        viewProperty.type = "spinner"

        //Add options
        val spinnerOption1 = NFormSubViewProperty().apply {
            name = "dont_know"
            text = "Don't know"
        }
        val spinnerOption2 = NFormSubViewProperty().apply {
            name = "female"
            text = "Female"
        }
        val spinnerOption3 = NFormSubViewProperty().apply {
            name = "male"
            text = "Male"
        }

        spinnerNFormView.viewProperties =
            viewProperty.apply { options = listOf(spinnerOption1, spinnerOption2, spinnerOption3) }
        spinnerViewBuilder = spinnerNFormView.viewBuilder
    }

    @Test
    fun `Should set label for spinner view`() {
        val text = "Pick gender"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        spinnerViewBuilder.buildView()
        val view = spinnerNFormView.getChildAt(0)

        Assert.assertTrue(view != null && view is SmartMaterialSpinner<*>)
        Assert.assertTrue((view as SmartMaterialSpinner<*>).hint.toString() == text)
    }

    @Test
    fun `Should set required on label required `() {
        val text = "Pick gender"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.requiredStatus = "yes:Am required"
        spinnerViewBuilder.buildView()
        val view = spinnerNFormView.getChildAt(0)
        Assert.assertTrue(
            (view as SmartMaterialSpinner<*>).hint.toString().isNotEmpty() && view.hint.toString()
                .endsWith("*")
        )
    }

    @Test
    fun `Should add options to the spinner`() {
        val text = "Pick gender"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        spinnerViewBuilder.buildView()
        Assert.assertTrue(spinnerNFormView.childCount == 1)
        val view = spinnerNFormView.getChildAt(0)
        //First item is Label and the other
        Assert.assertTrue(view is SmartMaterialSpinner<*>)
        Assert.assertTrue(spinnerNFormView.getChildAt(0) is SmartMaterialSpinner<*>)
        //Testing that spinner options were created
        val materialSpinner = spinnerNFormView.getChildAt(0) as SmartMaterialSpinner<*>
        Assert.assertTrue(materialSpinner.item[0] == "Don't know")
        Assert.assertTrue(materialSpinner.item[1] == "Female")
        Assert.assertTrue(materialSpinner.item[2] == "Male")
    }

    @Test
    fun `Should reset spinner value when the view is gone`() {
        val text = "Pick gender"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        spinnerNFormView.setupView(viewProperty, formBuilder)
        spinnerNFormView.visibility = View.GONE
        Assert.assertNull(spinnerNFormView.viewDetails.value)
        spinnerNFormView.resetValueWhenHidden()
        Assert.assertNull(spinnerNFormView.viewDetails.value)
    }

    @Test
    fun `Should set spinner value when an option is selected`() {
        val text = "Pick your gender"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        spinnerViewBuilder.buildView()
        val materialSpinner = spinnerNFormView.getChildAt(0) as SmartMaterialSpinner<*>
        materialSpinner.setSelection(1, false)
        Assert.assertEquals(materialSpinner.item[materialSpinner.selectedItemPosition], "Female")
    }

    @Test
    fun `Should set value on spinner when provided`() {
        viewProperty.viewAttributes = hashMapOf("text" to "Pick your gender")
        spinnerViewBuilder.buildView()
        val valueHashMap = mapOf("value" to "Female")
        spinnerNFormView.setValue(valueHashMap)
        Assert.assertEquals(spinnerNFormView.initialValue, valueHashMap)
        val materialSpinner = spinnerNFormView.viewBuilder.materialSpinner
        Assert.assertEquals(materialSpinner.item[materialSpinner.selectedItemPosition], "Female")
        spinnerNFormView.setValue(NFormViewData(value = "Male"))
        Assert.assertEquals(materialSpinner.item[materialSpinner.selectedItemPosition], "Male")
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}