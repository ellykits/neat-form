package com.nerdstone.neatformcore.junit.utils

import com.nerdstone.neatformcore.utils.convertEnumToSet
import com.nerdstone.neatformcore.utils.extractKeyValue
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    private enum class TestEnum {
        THREE, TWO, ONE, ZERO
    }

    private enum class EmptyEnum

    @Test
    fun `Should extract a key value from colon separated text`() {
        assertEquals(Pair("yes", "am required"), "yes:am required".extractKeyValue())
    }

    @Test
    fun `Should return a set when provided with enum`() {
        assertEquals(
            hashSetOf("THREE", "TWO", "ONE", "ZERO") as Any?,
            TestEnum::class.java.convertEnumToSet()
        )
    }

    @Test
    fun `Should return an empty set when provided with empty enum`() {
        val emptyHashSet: HashSet<Any?> = HashSet()
        assertEquals(emptyHashSet, EmptyEnum::class.java.convertEnumToSet())
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}