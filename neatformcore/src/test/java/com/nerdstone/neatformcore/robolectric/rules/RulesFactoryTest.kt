package com.nerdstone.neatformcore.robolectric.rules

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.rules.NFormRulesHandler
import com.nerdstone.neatformcore.rules.RulesFactory
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verifySequence
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class `Test Rules Engine functionality` {

    private val view: View = View(RuntimeEnvironment.systemContext)
    private val mainLayout: ViewGroup = LinearLayout(RuntimeEnvironment.systemContext)
    private val viewDetails = NFormViewDetails(view)
    private val rulesFactory = spyk<RulesFactory>(recordPrivateCalls = true)
    private val rulesHandler = NFormRulesHandler.INSTANCE

    @Before
    fun `Before doing anything else`() {
        view.id = 1
        viewDetails.name = "name"
        viewDetails.value = "20"
        rulesFactory.subjectsRegistry["name"] = hashSetOf() //map field to subjects

        //Setup rules handler with form builder and views map
        rulesHandler.viewIdsMap["name"] = 1
        rulesHandler.formBuilder = JsonFormBuilder(mainLayout)
        every { rulesFactory.rulesHandler } returns rulesHandler

    }

    @Test
    fun `Verify that rules are fired`() {
        rulesFactory.updateFactsAndExecuteRules(viewDetails)
        verifySequence {
            rulesFactory.subjectsRegistry
            rulesFactory invoke "updateFactsAndExecuteRules" withArguments listOf(viewDetails)
            rulesFactory invoke "updateCurrentViewAndFacts" withArguments listOf(viewDetails)
            rulesFactory invokeNoArgs "updateExecutableRules"
            rulesFactory invokeNoArgs "fireRules"
        }
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}