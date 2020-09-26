package com.nerdstone.neatformcore.robolectric.utils

import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.FragmentActivity
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.robolectric.builders.BaseJsonViewBuilderTest
import com.nerdstone.neatformcore.utils.addRedAsteriskSuffix
import com.nerdstone.neatformcore.utils.getSupportedEditTextTypes
import com.nerdstone.neatformcore.utils.setupView
import com.nerdstone.neatformcore.utils.updateFieldValues
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class ViewUtilsTest : BaseJsonViewBuilderTest() {

    @Test
    fun `Should append red asterisk at the end of EditText hint`() {
        val mockEditText =
            spyk(AppCompatEditText(RuntimeEnvironment.systemContext), recordPrivateCalls = false)
        mockEditText.hint = "Am a required hint"
        val originalHintLength = mockEditText.hint.toString().length
        mockEditText.hint = mockEditText.hint.toString().addRedAsteriskSuffix()
        val finalHintLength = mockEditText.hint.toString().length
        Assert.assertEquals(originalHintLength + 2, finalHintLength)
        Assert.assertTrue(mockEditText.hint.toString().endsWith("*"))
    }

    @Test
    fun `Should return a map of supported input types`() {
        Assert.assertEquals(getSupportedEditTextTypes().size, 31)
        Assert.assertTrue(getSupportedEditTextTypes()["phone"] is Int)
        Assert.assertTrue(getSupportedEditTextTypes().containsKey("number"))
    }

    @Test
    fun `Should update field values`() {

        val fieldValues = hashMapOf("age" to NFormViewData(value = 25, type = "EditTextNFormView"))
        val activity = Robolectric.buildActivity(FragmentActivity::class.java).get()
        val editText = EditTextNFormView(activity)
        editText.setupView(NFormViewProperty()
            .apply {
                name = "age"
                type = "edit_text"
            }, formBuilder
        )
        activity.setContentView(LinearLayout(activity).apply { addView(editText) })
        activity.updateFieldValues(fieldValues, mutableSetOf())
        Assert.assertEquals(editText.initialValue, 25)
        Assert.assertEquals(editText.viewDetails.value, 25.toString())
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}