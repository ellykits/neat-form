package com.nerdstone.neatformcore.junit.utils

import com.nerdstone.neatformcore.utils.ViewUtils
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class `Test View Utils` {

    @Before
    fun `Before doing anything else`() {
        mockkObject(ViewUtils)
    }

    @Test
    fun `Should split a string with a delimiter and return an array`() {
        val splitText = ViewUtils.splitText("age:number, child:text", ",")
        Assert.assertEquals(2, splitText.size)
        Assert.assertEquals("age:number", splitText[0])
        Assert.assertEquals(" child:text", splitText[1])
    }

    @Test
    fun `Should return string before given suffix`() {
        Assert.assertEquals("age", ViewUtils.getKey("age_visibility", "_visibility"))
        Assert.assertEquals("age_visibility", ViewUtils.getKey("age_visibility", "visible"))
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}