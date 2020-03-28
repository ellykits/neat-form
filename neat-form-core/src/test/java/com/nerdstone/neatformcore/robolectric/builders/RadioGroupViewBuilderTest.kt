package com.nerdstone.neatformcore.robolectric.builders

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormSubViewProperty
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.builders.RadioGroupViewBuilder
import com.nerdstone.neatformcore.views.containers.RadioGroupView
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
class `Test building RadioGroup view` : BaseJsonViewBuilderTest() {

    private val viewProperty = spyk(NFormViewProperty())
    private val radioOption1 = spyk(NFormSubViewProperty())
    private val radioOption2 = spyk(NFormSubViewProperty())
    private val radioOption3 = spyk(NFormSubViewProperty())
    private val radioGroupView = RadioGroupView(activity.get())
    private val radioGroupViewBuilder = spyk(RadioGroupViewBuilder(radioGroupView))

    @Before
    fun `Before doing anything else`() {
        radioGroupView.formValidator = this.formValidator
        viewProperty.name = "choose_language"
        viewProperty.type = "radio_group"

        //Set options
        radioOption1.name = "kotlin"
        radioOption1.text = "Kotlin"

        radioOption2.name = "java"
        radioOption2.text = "Java"

        radioOption3.name = "none"
        radioOption3.text = "None"

        //Set EditText properties and assign EditText view builder
        radioGroupView.viewProperties = viewProperty
    }

    @Test
    fun `Should set label for radio group`() {
        val text = "Pick your preferred Android programming language"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        radioGroupViewBuilder.buildView()
        val view = radioGroupView.getChildAt(0)
        val textView = view.findViewById<TextView>(R.id.labelTextView)
        Assert.assertTrue(textView != null)
        Assert.assertTrue(textView.text.toString() == text)
        Assert.assertTrue(radioGroupView.findViewById<TextView>(R.id.errorMessageTextView).visibility == View.GONE)
    }

    @Test
    fun `Should set required on label required `() {
        val text = "Pick your preferred Android programming language"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.requiredStatus = "yes:Am required"
        radioGroupViewBuilder.buildView()
        val view = radioGroupView.getChildAt(0)
        val textView = view.findViewById<TextView>(R.id.labelTextView)
        Assert.assertTrue(
            textView.text.toString().isNotEmpty() && textView.text.toString().endsWith("*")
        )
    }

    @Test
    fun `Should create multiple radio button options`() {
        val text = "Pick your preferred Android programming language"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.options = listOf(radioOption1, radioOption2, radioOption3)
        radioGroupViewBuilder.buildView()
        val view = radioGroupView.getChildAt(0)
        //First item is Label the rest are radio buttons
        Assert.assertTrue(view.findViewById<View>(R.id.labelTextView) is TextView)
        (1 until radioGroupView.childCount).forEach { i ->
            Assert.assertTrue(radioGroupView.getChildAt(i) is RadioButton)
        }

        //Check single RadioButton properties
        Assert.assertTrue((radioGroupView.getChildAt(1) as RadioButton).text == "Kotlin")
        Assert.assertTrue((radioGroupView.getChildAt(1) as RadioButton).getTag(R.id.field_name) == "kotlin")
        Assert.assertTrue((radioGroupView.getChildAt(1) as RadioButton).getTag(R.id.is_radio_group_option) == true)

        Assert.assertTrue((radioGroupView.getChildAt(2) as RadioButton).text == "Java")
        Assert.assertTrue((radioGroupView.getChildAt(2) as RadioButton).getTag(R.id.field_name) == "java")
        Assert.assertTrue((radioGroupView.getChildAt(2) as RadioButton).getTag(R.id.is_radio_group_option) == true)

        Assert.assertTrue((radioGroupView.getChildAt(3) as RadioButton).text == "None")
        Assert.assertTrue((radioGroupView.getChildAt(3) as RadioButton).getTag(R.id.field_name) == "none")
        Assert.assertTrue((radioGroupView.getChildAt(3) as RadioButton).getTag(R.id.is_radio_group_option) == true)


        //All views counted including label
        Assert.assertTrue(radioGroupView.childCount == 4)
    }

    @Test
    fun `Should set ViewDetails value to a map of name and label of the selected radio button option`() {
        val text = "Pick your preferred Android programming language"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.options = listOf(radioOption1, radioOption2, radioOption3)
        ViewUtils.setupView(radioGroupView, viewProperty, spyk())
        //Value is null first but will always contain a value when selection is done
        Assert.assertNull(radioGroupView.viewDetails.value)

        //Map value should contain a value
        val radioButton1 = radioGroupView.getChildAt(1) as RadioButton
        radioButton1.isChecked = true

        Assert.assertTrue(radioGroupView.viewDetails.value != null && (radioGroupView.viewDetails.value as HashMap<*, *>).size == 1)
        Assert.assertTrue((radioGroupView.viewDetails.value as HashMap<*, *>).containsKey("kotlin"))
        Assert.assertTrue(((radioGroupView.viewDetails.value as HashMap<*, *>)["kotlin"]!! as NFormViewData).value == radioButton1.text)
    }

    @Test
    fun `Should uncheck all the radio buttons when the view is gone`() {
        val text = "Pick your preferred Android programming language"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.options = listOf(radioOption1, radioOption2, radioOption3)
        ViewUtils.setupView(radioGroupView, viewProperty, spyk())
        val radioButton1 = radioGroupView.getChildAt(1) as RadioButton
        radioButton1.isChecked = true
        radioGroupView.visibility = View.GONE

        (1 until radioGroupView.childCount).forEach { i ->
            Assert.assertTrue(!(radioGroupView.getChildAt(i) as RadioButton).isChecked)
        }
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}