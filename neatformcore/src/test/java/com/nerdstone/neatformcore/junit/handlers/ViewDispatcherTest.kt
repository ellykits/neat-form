package com.nerdstone.neatformcore.junit.handlers

import com.nerdstone.neatformcore.domain.model.NFormRule
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class ViewDispatcherTest {

    private val viewDispatcher = spyk<ViewDispatcher>()
    private val viewDetails = spyk(NFormViewDetails(mockk(relaxed = false)))

    @Before
    fun `Before everything else`() {
        viewDetails.name = "name"
        every { viewDispatcher.rulesFactory.subjectsRegistry } returns hashMapOf()
    }

    @Test
    @Ignore("Need to figure out  why verify is not working")
    fun `Ensure rule is fired only for dependant fields`() {
        viewDispatcher.rulesFactory.subjectsRegistry["name"] =
            hashSetOf(NFormRule(key = "age", matchingRules = hashSetOf()))
        every { viewDispatcher.onPassData(viewDetails) } just runs
        verify { viewDispatcher.rulesFactory.updateFactsAndExecuteRules(any()) }
        confirmVerified(viewDispatcher)
    }

    @Test
    @Ignore("Need to figure out  why verify is not working with this too")
    fun `Ensure rule is not fired when no fields depends on current input`() {
        viewDispatcher.rulesFactory.subjectsRegistry.clear()
        val viewDetails = spyk(NFormViewDetails(mockk(relaxed = false)))
        viewDetails.name = "name"
        viewDispatcher.onPassData(viewDetails)
        verify { viewDispatcher.rulesFactory.updateFactsAndExecuteRules(any()) wasNot called }
        confirmVerified(viewDispatcher)
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}
