package com.nerdstone.neatformcore.junit.utils

import com.nerdstone.neatformcore.utils.getKey
import com.nerdstone.neatformcore.utils.splitText
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Test

class ViewUtilsTest {

    @Test
    fun `Should split a string with a delimiter and return an array`() {
        val splitText = "age:number, child:text".splitText( ",")
        Assert.assertEquals(2, splitText.size)
        Assert.assertEquals("age:number", splitText[0])
        Assert.assertEquals(" child:text", splitText[1])
    }

    @Test
    fun `Should return string before given suffix`() {
        Assert.assertEquals("age", "age_visibility".getKey("_visibility"))
        Assert.assertEquals("age_visibility", "age_visibility".getKey("visible"))
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}