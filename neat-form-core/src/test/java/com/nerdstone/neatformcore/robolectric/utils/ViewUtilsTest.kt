package com.nerdstone.neatformcore.robolectric.utils

import androidx.appcompat.widget.AppCompatEditText
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.utils.ViewUtils
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class ViewUtilsTest {

    @Before
    fun `Before doing anything else`() {
        mockkObject(ViewUtils)
    }


    @Test
    fun `Should append red asterisk at the end of EditText hint`() {
        val mockEditText =
            spyk(AppCompatEditText(RuntimeEnvironment.systemContext), recordPrivateCalls = false)
        mockEditText.hint = "Am a required hint"
        val originalHintLength = mockEditText.hint.toString().length
        mockEditText.hint = ViewUtils.addRedAsteriskSuffix(mockEditText.hint.toString())
        val finalHintLength = mockEditText.hint.toString().length
        Assert.assertEquals(originalHintLength + 2, finalHintLength)
        Assert.assertTrue(mockEditText.hint.toString().endsWith("*"))
    }

    @Test
    fun `Should return a map of supported input types`(){
        Assert.assertEquals(ViewUtils.getSupportedEditTextTypes().size, 31)
        Assert.assertTrue(ViewUtils.getSupportedEditTextTypes()["phone"] is Int)
        Assert.assertTrue(ViewUtils.getSupportedEditTextTypes().containsKey("number"))
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}