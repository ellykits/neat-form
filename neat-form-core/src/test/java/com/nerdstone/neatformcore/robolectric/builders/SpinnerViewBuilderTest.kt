package com.nerdstone.neatformcore.robolectric.builders

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormSubViewProperty
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.views.builders.SpinnerViewBuilder
import com.nerdstone.neatformcore.views.widgets.SpinnerNFormView
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class `Test building Spinner view` {

    private val viewProperty = spyk(NFormViewProperty())
    private val spinnerOption1 = spyk(NFormSubViewProperty())
    private val spinnerOption2 = spyk(NFormSubViewProperty())
    private val spinnerOption3 = spyk(NFormSubViewProperty())
    private val activity = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
    private val spinnerNFormView = SpinnerNFormView(activity.get())
    private val spinnerViewBuilder =
        spyk(objToCopy = SpinnerViewBuilder(spinnerNFormView), recordPrivateCalls = true)

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "gender"
        viewProperty.type = "spinner"
        spinnerNFormView.viewProperties = viewProperty

        //Add options
        spinnerOption1.name = "dont_know"
        spinnerOption1.text = "Don't know"

        spinnerOption2.name = "female"
        spinnerOption2.text = "Female"

        spinnerOption3.name = "male"
        spinnerOption3.text = "Male"


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
            (view as SmartMaterialSpinner<*>).hint.toString().isNotEmpty() &&  view.hint.toString().endsWith("*")
        )
    }

    @Test
    fun `Should add options to the spinner`() {
        val text = "Pick gender"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.options =
            listOf(spinnerOption1, spinnerOption2, spinnerOption3)
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
        viewProperty.options =
            listOf(spinnerOption1, spinnerOption2, spinnerOption3)
        spinnerNFormView.initView(viewProperty, spyk())
        spinnerNFormView.visibility = View.GONE
        Assert.assertNull(spinnerNFormView.viewDetails.value)
    }

    @Test
    fun `Should set spinner value when an option is selected`() {
        val text = "Pick your gender"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.options =
            listOf(spinnerOption1, spinnerOption2, spinnerOption3)
        spinnerNFormView.initView(viewProperty, spyk())
        val materialSpinner = spinnerNFormView.getChildAt(0) as SmartMaterialSpinner<*>
        materialSpinner.setSelection(1)
        materialSpinner.isSelected = true
        Assert.assertTrue(materialSpinner.item[materialSpinner.selectedItemPosition]  == "Female")
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}