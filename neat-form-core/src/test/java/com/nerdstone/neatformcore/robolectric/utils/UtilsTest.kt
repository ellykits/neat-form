package com.nerdstone.neatformcore.robolectric.utils

import android.util.TypedValue
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.utils.Utils
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
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
class `Test Utils with Robolectric` {
    @Before
    fun `Before everything else`() {
        mockkObject(Utils)
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }

    @Test
    fun `Should convert px to dp`() {
        mockkStatic(TypedValue::class)
        Utils.pxToDp(16.0f, RuntimeEnvironment.systemContext)
        verify {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16.0f,
                RuntimeEnvironment.systemContext.resources.displayMetrics
            ).toInt()
        }
        Assert.assertEquals(16, Utils.pxToDp(16.0f, RuntimeEnvironment.systemContext))
    }
}