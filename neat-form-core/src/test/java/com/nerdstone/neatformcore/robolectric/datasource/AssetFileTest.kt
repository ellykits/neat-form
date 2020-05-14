package com.nerdstone.neatformcore.robolectric.datasource

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.nerdstone.neatformcore.TestConstants
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.datasource.AssetFile
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class AssetFileTest {

    private lateinit var context: Context

    @Before
    fun `Before everything else`() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `Read file should work well`() {
        val content = AssetFile
            .readAssetFileAsString(context, TestConstants.SAMPLE_ONE_FORM_FILE)
        Assert.assertNotNull(content)
        Assert.assertTrue(content.contains("Profile"))

    }
}
