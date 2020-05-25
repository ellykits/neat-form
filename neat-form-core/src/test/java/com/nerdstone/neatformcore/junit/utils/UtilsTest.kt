package com.nerdstone.neatformcore.junit.utils

import com.nerdstone.neatformcore.utils.Utils
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UtilsTest {

    private enum class TestEnum {
        THREE, TWO, ONE, ZERO
    }

    private enum class EmptyEnum

    @Before
    fun `Before doing anything else`() {
        mockkObject(Utils)
    }

    @Test
    fun `Should extract a key value from colon separated text`() {
        assertEquals(Pair("yes", "am required"), Utils.extractKeyValue("yes:am required"))
    }

    @Test
    fun `Should return a set when provided with enum`() {
        assertEquals(
            hashSetOf("THREE", "TWO", "ONE", "ZERO") as Any?,
            Utils.convertEnumToSet(TestEnum::class.java)
        )
    }

    @Test
    fun `Should return an empty set when provided with empty enum`() {
        val emptyHashSet: HashSet<Any?> = HashSet()
        assertEquals(emptyHashSet, Utils.convertEnumToSet(EmptyEnum::class.java))
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}