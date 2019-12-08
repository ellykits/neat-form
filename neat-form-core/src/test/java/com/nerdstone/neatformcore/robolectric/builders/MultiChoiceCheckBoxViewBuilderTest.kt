package com.nerdstone.neatformcore.robolectric.builders

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormSubViewProperty
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.views.builders.MultiChoiceCheckBoxViewBuilder
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
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
class `Test building MultiChoiceCheckBox view`: BaseJsonViewBuilderTest() {

    private val viewProperty = spyk(NFormViewProperty())
    private val checkBoxOption1 = spyk(NFormSubViewProperty())
    private val checkBoxOption2 = spyk(NFormSubViewProperty())
    private val checkBoxOption3 = spyk(NFormSubViewProperty())
    private val checkBoxOption4 = spyk(NFormSubViewProperty())
    private val activity = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
    private val multiChoiceCheckBox = MultiChoiceCheckBox(activity.get())
    private val multiChoiceCheckBoxViewBuilder =
        spyk(MultiChoiceCheckBoxViewBuilder(multiChoiceCheckBox))

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "choose_language"
        viewProperty.type = "multi_choice_checkbox"

        //Set options
        checkBoxOption1.name = "kotlin"
        checkBoxOption1.text = "Kotlin"

        checkBoxOption2.name = "java"
        checkBoxOption2.text = "Java"

        checkBoxOption3.name = "dont_know"
        checkBoxOption3.isExclusive = true
        checkBoxOption3.text = "Don't know"

        checkBoxOption4.name = "none"
        checkBoxOption4.isExclusive = true
        checkBoxOption4.text = "None"
        checkBoxOption4.viewAttributes = hashMapOf()
        multiChoiceCheckBox.formValidator = this.formValidator
        multiChoiceCheckBox.viewProperties = viewProperty
    }

    @Test
    fun `Should set label for multi choice checkbox`() {
        val text = "Pick the programming languages"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        multiChoiceCheckBoxViewBuilder.buildView()
        val view = multiChoiceCheckBox.getChildAt(0)

        val textView = view.findViewById<TextView>(R.id.labelTextView)
        Assert.assertTrue(textView.text.toString() == text)
        Assert.assertTrue(multiChoiceCheckBox.findViewById<TextView>(R.id.errorMessageTextView).visibility == View.GONE)
    }

    @Test
    fun `Should set required on label required `() {
        val text = "Am a required field"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.requiredStatus = "yes:Am required"
        multiChoiceCheckBoxViewBuilder.buildView()
        val view = multiChoiceCheckBox.getChildAt(0)
        val textView = view.findViewById<TextView>(R.id.labelTextView)
        Assert.assertTrue(
            textView.text.toString().isNotEmpty() && textView.text.toString().endsWith("*")
        )
    }

    @Test
    fun `Should create multiple checkboxes when options are defined`() {
        val text = "Pick your programming languages"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.options =
            listOf(checkBoxOption1, checkBoxOption2, checkBoxOption3, checkBoxOption4)
        multiChoiceCheckBoxViewBuilder.buildView()
        val view = multiChoiceCheckBox.getChildAt(0)
        //First item is Label the rest are checkboxes
        Assert.assertTrue(view.findViewById<View>(R.id.labelTextView) is TextView)
        (1 until multiChoiceCheckBox.childCount).forEach { i ->
            Assert.assertTrue(multiChoiceCheckBox.getChildAt(i) is CheckBox)
        }

        //Check single non-exclusive checkbox properties
        Assert.assertTrue((multiChoiceCheckBox.getChildAt(1) as CheckBox).text == "Kotlin")
        Assert.assertTrue((multiChoiceCheckBox.getChildAt(1) as CheckBox).getTag(R.id.field_name) == "kotlin")
        Assert.assertTrue((multiChoiceCheckBox.getChildAt(1) as CheckBox).getTag(R.id.is_exclusive_checkbox) == null)
        Assert.assertTrue((multiChoiceCheckBox.getChildAt(1) as CheckBox).getTag(R.id.is_checkbox_option) == true)

        //Check single exclusive checkbox properties
        Assert.assertTrue((multiChoiceCheckBox.getChildAt(4) as CheckBox).text == "None")
        Assert.assertTrue((multiChoiceCheckBox.getChildAt(4) as CheckBox).getTag(R.id.field_name) == "none")
        Assert.assertTrue((multiChoiceCheckBox.getChildAt(4) as CheckBox).getTag(R.id.is_exclusive_checkbox) == true)
        Assert.assertTrue((multiChoiceCheckBox.getChildAt(4) as CheckBox).getTag(R.id.is_checkbox_option) == true)

        //All views counted including label
        Assert.assertTrue(multiChoiceCheckBox.childCount == 5)
    }

    @Test
    fun `Should set ViewDetails value to a map of name and label of the selected checkboxes`() {
        val text = "Pick your programming languages"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.options =
            listOf(checkBoxOption1, checkBoxOption2, checkBoxOption3, checkBoxOption4)
        multiChoiceCheckBox.initView(viewProperty, spyk())

        //Select 2 checkboxes and ensure value map contains the 2 checkboxes
        val checkBox1 = multiChoiceCheckBox.getChildAt(1) as CheckBox
        val checkBox2 = multiChoiceCheckBox.getChildAt(2) as CheckBox

        checkBox1.isChecked = true
        checkBox2.isChecked = true

        Assert.assertTrue(multiChoiceCheckBox.viewDetails.value != null && (multiChoiceCheckBox.viewDetails.value as HashMap<*, *>).size == 2)
        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<*, *>).containsKey("kotlin"))
        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<*, *>)["kotlin"]!! == checkBox1.text)
        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<*, *>).containsKey("java"))
        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<*, *>)["java"]!! == checkBox2.text)

        //When  first checkbox is deselected then the value should be set to null
        checkBox1.isChecked = false
        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<*, *>).containsKey("kotlin"))
        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<String, String?>)["kotlin"] == null)
    }

    @Test
    fun `Should deselect all the other checkbox when the option is exclusive`() {
        val text = "Pick your programming languages"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.options =
            listOf(checkBoxOption1, checkBoxOption2, checkBoxOption3, checkBoxOption4)
        multiChoiceCheckBox.initView(viewProperty, spyk())

        //An exclusive option can only be selected independently
        val checkBox1 = multiChoiceCheckBox.getChildAt(1) as CheckBox //kotlin
        val checkBox3 = multiChoiceCheckBox.getChildAt(3) as CheckBox //don't know
        val checkBox4 = multiChoiceCheckBox.getChildAt(4) as CheckBox //none

        checkBox1.isChecked = true
        checkBox4.isChecked = true
        checkBox3.isChecked = true

        //Since checkbox 3 is exclusive and can only be selected independently the other value must be reset to null
        Assert.assertTrue(multiChoiceCheckBox.viewDetails.value != null && (multiChoiceCheckBox.viewDetails.value as HashMap<*, *>).size == 3)
        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<*, *>).containsKey("kotlin"))
        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<*, String?>)["kotlin"] == null)
        Assert.assertTrue(!checkBox1.isChecked)

        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<*, *>).containsKey("none"))
        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<*, String?>)["none"] == null)
        Assert.assertTrue(!checkBox4.isChecked)

        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<*, *>).containsKey("dont_know"))
        Assert.assertTrue((multiChoiceCheckBox.viewDetails.value as HashMap<*, String?>)["dont_know"] == "Don't know")

    }

    @Test
    fun `Should uncheck all the checkboxes when the view is gone`() {
        val text = "Pick your programming languages"
        viewProperty.viewAttributes = hashMapOf("text" to text)
        viewProperty.options =
            listOf(checkBoxOption1, checkBoxOption2, checkBoxOption3, checkBoxOption4)
        multiChoiceCheckBox.initView(viewProperty, spyk())
        val checkBox1 = multiChoiceCheckBox.getChildAt(1) as CheckBox
        val checkBox2 = multiChoiceCheckBox.getChildAt(2) as CheckBox
        checkBox1.isChecked = true
        checkBox2.isChecked = true
        multiChoiceCheckBox.visibility = View.GONE

        (1 until multiChoiceCheckBox.childCount).forEach { i ->
            Assert.assertTrue(!(multiChoiceCheckBox.getChildAt(i) as CheckBox).isChecked)
        }

    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}