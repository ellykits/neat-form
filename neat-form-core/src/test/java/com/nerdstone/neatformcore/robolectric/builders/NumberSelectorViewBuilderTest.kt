package com.nerdstone.neatformcore.robolectric.builders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.views.builders.NumberSelectorViewBuilder
import com.nerdstone.neatformcore.views.widgets.NumberSelectorNFormView
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
class `Test building NumberSelector view` {
    private val activity = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
    private val numberSelector =
        NumberSelectorNFormView(activity.get())
    private val numberSelectorViewBuilder = spyk(NumberSelectorViewBuilder(numberSelector))
    private val viewProperty = spyk(NFormViewProperty())

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "number_selector_test"
        viewProperty.type = "number_selector"
        numberSelector.viewProperties = viewProperty
        viewProperty.viewAttributes = hashMapOf(
            "text" to "Number of previous pregnancies",
            "visible_numbers" to "5",
            "max_value" to "10",
            "first_number" to "0"
        )
    }

    @Test
    fun `Should set label for number selector`() {
        numberSelectorViewBuilder.buildView()
        val view = numberSelector.getChildAt(0)
        Assert.assertTrue(view != null && view is TextView)
        Assert.assertTrue((view as TextView).text.toString() == "Number of previous pregnancies")
    }

    @Test
    fun `Should set required on label required `() {
        viewProperty.requiredStatus = "yes:Am required"
        numberSelectorViewBuilder.buildView()
        val view = numberSelector.getChildAt(0)
        Assert.assertTrue(
            (view as TextView).text.toString().isNotEmpty() && view.text.toString().endsWith("*")
        )
    }

    @Test
    fun `Should display exact specified numbers on the selector`() {
        numberSelectorViewBuilder.buildView()
        Assert.assertTrue(numberSelector.childCount == 2)
        val view = numberSelector.getChildAt(1) as LinearLayout
        Assert.assertTrue(view.childCount == 6)
        Assert.assertTrue((view.getChildAt(0) as TextView).text == "0")
        Assert.assertTrue((view.getChildAt(1) as TextView).text == "1")
        Assert.assertTrue((view.getChildAt(2) as TextView).text == "2")
        Assert.assertTrue((view.getChildAt(3) as TextView).text == "3")
        Assert.assertTrue((view.getChildAt(4) as TextView).text == "4")
        Assert.assertTrue((view.getChildAt(5) as TextView).text == "5 +")

    }

    @Test
    fun `Should create number selector with one item`() {
        viewProperty.viewAttributes = hashMapOf(
            "text" to "Number of previous pregnancies",
            "visible_numbers" to "1",
            "max_value" to "1",
            "first_number" to "1"
        )
        numberSelectorViewBuilder.buildView()
        val view = numberSelector.getChildAt(1) as LinearLayout
        Assert.assertTrue(view.childCount == 1)
        Assert.assertTrue((view.getChildAt(0) as TextView).text == "1")
    }

    @Test
    fun `Should set pass value as integer when number is selected`() {
        numberSelector.initView(viewProperty, spyk())

        //Value is null first but will always contain a value when selection is done
        Assert.assertNull(numberSelector.viewDetails.value)

        val linearLayout = numberSelector.getChildAt(1) as LinearLayout
        val textView3 = linearLayout.getChildAt(3) as TextView
        val textView5 = linearLayout.getChildAt(5) as TextView
        textView3.performClick()
        Assert.assertTrue(numberSelector.viewDetails.value != null && numberSelector.viewDetails.value == 3)
        textView5.performClick()
        Assert.assertTrue(numberSelector.viewDetails.value != null && numberSelector.viewDetails.value == 3)
    }

    @Test
    fun `Should deselect number when the number selector view is gone`() {
        numberSelector.initView(viewProperty, spyk())
        val linearLayout = numberSelector.getChildAt(1) as LinearLayout
        val textView = linearLayout.getChildAt(3) as TextView
        textView.performClick()
        numberSelector.visibility = View.GONE
        Assert.assertNull(numberSelector.viewDetails.value)
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}