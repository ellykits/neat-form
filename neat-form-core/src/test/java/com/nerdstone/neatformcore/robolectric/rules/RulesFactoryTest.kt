package com.nerdstone.neatformcore.robolectric.rules

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.nerdstone.neatformcore.TestConstants
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormRule
import com.nerdstone.neatformcore.domain.model.NFormViewDetails
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.rules.NFormRulesHandler
import com.nerdstone.neatformcore.rules.RulesFactory
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import org.jeasy.rules.api.Condition
import org.jeasy.rules.api.Rule
import org.jeasy.rules.api.Rules
import org.jeasy.rules.core.RuleBuilder
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class `Test Rules Engine functionality` {

    private val view: View = View(RuntimeEnvironment.systemContext)
    private val mainLayout: ViewGroup = LinearLayout(RuntimeEnvironment.systemContext)
    private val viewDetails = NFormViewDetails(view)
    private val rulesFactory = spyk<RulesFactory>(recordPrivateCalls = true)
    private val rulesHandler = NFormRulesHandler.INSTANCE
    private lateinit var rule1: Rule
    private lateinit var rule2: Rule

    @Before
    fun `Before doing anything else`() {
        setupRules()

        //Setup view details
        view.id = 1
        view.visibility = View.GONE

        //When age changes then adult field is affected
        rulesFactory.subjectsRegistry["age"] =
            hashSetOf(NFormRule("age", hashSetOf(rule1, rule2)))
        mainLayout.addView(view)

        //Setup rules handler with form builder and views map
        rulesHandler.viewIdsMap["adult"] = 1
        rulesHandler.formBuilder = JsonFormBuilder(, TestConstants.SAMPLE_ONE_FORM_FILE, mainLayout)
        every { rulesFactory.rulesHandler } returns rulesHandler
    }

    private fun setupRules() {
        /**
         * Here is the corresponding MVEL representation for {{rule1}}
         *
        name: "adult_visibility"
        description: "Show if age greater than 18"
        priority: 1
        condition: "age > 18"
        actions:
        - "adult_visibility =  true"
         */
        rule1 = RuleBuilder()
            .name("adult_visibility")
            .description("Show if age greater than 18")
            .priority(1)
            .`when` { facts ->
                val age: Int = facts.get<String>("age").toInt()
                age > 18
            }
            .then { facts -> facts.put("adult_visibility", true) }
            .build()

        /**
         *  Here is the corresponding MVEL representation for {{rule2}}
         *
        name: "decade_calculation"
        description: "Always perform this calculation"
        priority: 1
        condition: "true"
        actions:
        - "decade_calculation =  age * 10"
         */
        rule2 = RuleBuilder()
            .name("decade_calculation")
            .description("Always multiply by 10")
            .priority(1)
            .`when`(Condition.TRUE)
            .then { facts ->
                val decade = facts.asMap()["age"] as Int * 10
                facts.put("decade_calculation", decade)
            }
            .build()

        rulesFactory.allRules = Rules(rule1, rule2)
    }

    @Test
    fun `Testing skip logic with condition age (20) greater than 18 verify that age_visibility = true `() {
        //When age passed is 20 then age_visibility should return true
        viewDetails.name = "age"
        viewDetails.value = "20"

        //Register sample rules
        rulesFactory.updateFactsAndExecuteRules(viewDetails)
        verify {
            rulesFactory invoke "updateCurrentViewAndFacts" withArguments listOf(viewDetails)
            rulesFactory invokeNoArgs "updateExecutableRules"
            rulesFactory invokeNoArgs "fireRules"
        }

        //When age > 18 then set adult_visibility = true thus show view
        Assert.assertTrue(view.visibility == View.VISIBLE)

    }

    @Test
    fun `Testing skip logic with condition age (8) less than 18 verify that age_visibility = false `() {
        //When age passed is 8 then age_visibility should return false
        viewDetails.name = "age"
        viewDetails.value = "8"

        //update facts and fire rules
        rulesFactory.updateFactsAndExecuteRules(viewDetails)
        verify {
            rulesFactory invoke "updateCurrentViewAndFacts" withArguments listOf(viewDetails)
            rulesFactory invokeNoArgs "updateExecutableRules"
            rulesFactory invokeNoArgs "fireRules"
        }

        //When age < 18 then set adult_visibility = false thus hide view
        Assert.assertTrue(view.visibility == View.GONE)

    }

    @Test
    fun `Test whether view with skip logic rules defined are hidden when you first load the form`() {
        //If this view has a rule to handle its visibility then by default set it's visibility
        //To false when you first launch the form until when rules are fired
        view.visibility = View.VISIBLE
        rulesHandler.hideOrShowField("adult", false)
        Assert.assertTrue(view.visibility == View.GONE)
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}