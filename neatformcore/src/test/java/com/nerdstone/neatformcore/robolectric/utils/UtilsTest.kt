package com.nerdstone.neatformcore.robolectric.utils

import com.nerdstone.neatformcore.utils.Utils
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class `Utility Class Test - Robolectric` {

    @Before
    fun `Before everything else`() {
        mockkObject(Utils)
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }

}