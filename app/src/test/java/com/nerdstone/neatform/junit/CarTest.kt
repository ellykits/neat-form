package com.nerdstone.neatform.junit

import com.nerdstone.neatform.Car
import io.mockk.every
import io.mockk.spyk
import io.mockk.verifySequence
import junit.framework.Assert.assertEquals
import org.junit.Test

class CarTest {
    @Test
    fun testAccelerate(){
        val mock = spyk<Car>(recordPrivateCalls = true)

        every { mock["accelerate"]() } returns "going not so fast"

        assertEquals("going not so fast", mock.drive())

        verifySequence {
            mock.drive()
            mock["accelerate"]()
        }
    }
}