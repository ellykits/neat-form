package com.nerdstone.neatformcore.robolectric.utils

import android.util.TypedValue
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.utils.pxToDp
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class UtilsTest {

    @After
    fun `After everything else`() {
        unmockkAll()
    }

    @Test
    fun `Should convert px to dp`() {
        mockkStatic(TypedValue::class)
        RuntimeEnvironment.systemContext.pxToDp(16.0f)
        verify {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16.0f,
                RuntimeEnvironment.systemContext.resources.displayMetrics
            ).toInt()
        }
        Assert.assertEquals(16, RuntimeEnvironment.systemContext.pxToDp(16.0f))
    }
}