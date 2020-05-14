package com.nerdstone.neatformcore.robolectric.builders

import android.view.View
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.views.builders.DateTimePickerViewBuilder
import com.nerdstone.neatformcore.views.widgets.DateTimePickerNFormView
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
class DateTimePickerViewBuilderTest : BaseJsonViewBuilderTest(){

    private val viewProperty = spyk(NFormViewProperty())
    private val dateTimePickerNFormView = DateTimePickerNFormView(activity.get())
    private val dateTimePickerViewBuilder = spyk(
        objToCopy = DateTimePickerViewBuilder(dateTimePickerNFormView),
        recordPrivateCalls = true
    )

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "dob"
        viewProperty.type = "datetime_picker"
        //Set EditText properties and assign EditText view builder
        dateTimePickerNFormView.formValidator = this.formValidator
        dateTimePickerNFormView.viewProperties = viewProperty
    }

    @Test
    fun `Should set hint on the datetime picker`() {
        val hint = "Am a hint"
        viewProperty.viewAttributes = hashMapOf("hint" to hint, "display_format" to "dd/MM/yyyy")
        dateTimePickerViewBuilder.buildView()
        Assert.assertTrue(
            dateTimePickerViewBuilder.textInputEditText.hint!!.isNotEmpty() &&
                    dateTimePickerViewBuilder.textInputEditText.hint.toString() == hint
        )
    }

    @Test
    fun `Should set required on datetime hint `() {
        val hint = "Am a required field"
        viewProperty.viewAttributes = hashMapOf("hint" to hint)
        viewProperty.requiredStatus = "yes:Am required"
        dateTimePickerViewBuilder.buildView()
        Assert.assertTrue(
            dateTimePickerViewBuilder.textInputEditText.hint!!.isNotEmpty() && dateTimePickerViewBuilder.textInputEditText.hint!!.endsWith(
                "*"
            )
        )
    }

    @Test
    fun `Should reset the datetime picker  value when visibility is gone`() {
        dateTimePickerNFormView.visibility = View.GONE
        Assert.assertTrue(dateTimePickerViewBuilder.textInputEditText.text.toString().isEmpty())
        Assert.assertNull(dateTimePickerNFormView.viewDetails.value)
    }

    @Test
    fun `Ensure that the right icon is added for date picker`() {
        val hint = "Am a hint"
        val type = "date_picker"
        viewProperty.viewAttributes =
            hashMapOf("hint" to hint, "display_format" to "dd/MM/yyyy", "type" to type)
        dateTimePickerViewBuilder.buildView()
        Assert.assertTrue(dateTimePickerViewBuilder.textInputEditText.compoundDrawablePadding == 8)
        val compoundDrawables = dateTimePickerViewBuilder.textInputEditText.compoundDrawables
        Assert.assertNotNull(compoundDrawables)

    }

    @Test
    fun `Ensure that the right icon is added for time picker`() {
        val hint = "Am a hint"
        val type = "time_picker"
        viewProperty.viewAttributes =
            hashMapOf("hint" to hint, "display_format" to "dd/MM/yyyy", "type" to type)
        dateTimePickerViewBuilder.buildView()
        Assert.assertTrue(dateTimePickerViewBuilder.textInputEditText.compoundDrawablePadding == 8)
        val compoundDrawables = dateTimePickerViewBuilder.textInputEditText.compoundDrawables
        Assert.assertNotNull(compoundDrawables)
    }

    @Test
    fun `Should launch date picker dialog, display formatted time and update view details value`() {
        val hint = "Am a hint"
        val type = "date_picker"
        viewProperty.viewAttributes =
            hashMapOf("hint" to hint, "display_format" to "dd/MM/yyyy", "type" to type)
        dateTimePickerViewBuilder.buildView()
        dateTimePickerViewBuilder.textInputEditText.performClick()
        dateTimePickerViewBuilder.onDateSet(spyk(), 2019, 0,1)
        Assert.assertTrue(dateTimePickerViewBuilder.textInputEditText.text.toString() == "01/01/2019")
    }

    @Test
    fun `Should launch time picker dialog, display formatted time and update view details value`() {
        val hint = "Am a hint"
        val type = "time_picker"
        viewProperty.viewAttributes =
            hashMapOf("hint" to hint, "display_format" to "hh:m a", "type" to type)
        dateTimePickerViewBuilder.buildView()
        dateTimePickerViewBuilder.textInputEditText.performClick()
        dateTimePickerViewBuilder.onTimeSet(spyk(), 11, 30)
        Assert.assertTrue(dateTimePickerViewBuilder.textInputEditText.text.toString() == "11:30 AM")
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}