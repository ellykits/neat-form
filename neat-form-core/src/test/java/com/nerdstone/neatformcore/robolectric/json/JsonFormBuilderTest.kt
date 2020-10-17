package com.nerdstone.neatformcore.robolectric.json

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.nerdstone.neatandroidstepper.core.model.StepperModel
import com.nerdstone.neatandroidstepper.core.stepper.StepperPagerAdapter
import com.nerdstone.neatandroidstepper.core.widget.NeatStepperLayout
import com.nerdstone.neatformcore.CoroutineTestRule
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.TestConstants
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.form.json.*
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import com.nerdstone.neatformcore.views.containers.RadioGroupView
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import com.nerdstone.neatformcore.views.widgets.*
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
@ExperimentalCoroutinesApi
class JsonFormBuilderTest {

    private val activity = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
    private val mainLayout: LinearLayout = LinearLayout(activity.get())
    private val neatStepperLayout = NeatStepperLayout(activity.get())
    private lateinit var jsonFormBuilder: JsonFormBuilder
    private lateinit var jsonFormStepper: JsonFormStepper
    private lateinit var jsonFormEmbedded: JsonFormEmbedded
    private var observer: Observer<HashMap<String, NFormViewData>> = spyk()
    private val previousFormData = """
        {
            "age": {
              "meta_data": {
                "openmrs_entity": "",
                "openmrs_entity_id": "",
                "openmrs_entity_parent": ""
              },
              "type": "TextInputEditTextNFormView",
              "value": "54"
            },
            "child": {
              "type": "TextInputEditTextNFormView",
              "value": "yes"
            },
            "adult": {
              "type": "TextInputEditTextNFormView",
              "value": "0723721920"
            }
        }
    """.trimIndent()

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Test
    fun `Should parse json from file source, create views and register form rules`() =
        coroutinesTestRule.runBlockingTest {
            jsonFormBuilder = spyk(
                JsonFormBuilder(activity.get(), TestConstants.SAMPLE_ONE_FORM_FILE)
            )
            jsonFormBuilder.defaultContextProvider = coroutinesTestRule.testDispatcherProvider
            jsonFormEmbedded = JsonFormEmbedded(jsonFormBuilder, mainLayout).buildForm()
            Assert.assertNotNull(jsonFormBuilder.form)
            Assert.assertTrue(jsonFormBuilder.form?.steps?.size == 1)
            Assert.assertTrue(jsonFormBuilder.form?.steps?.get(0)?.stepName == "Test and counselling")

            //Main layout has on element: VerticalRootView inside a ScrollView
            Assert.assertTrue(mainLayout.childCount == 1)
            Assert.assertTrue(mainLayout.getChildAt(0) is ScrollView)
            val scrollView = mainLayout.getChildAt(0) as ScrollView
            //VerticalRootView has 3 EditTextNFormView
            val verticalRootView = scrollView.getChildAt(0) as VerticalRootView
            Assert.assertTrue(verticalRootView.childCount == 14)
            Assert.assertTrue(verticalRootView.getChildAt(0) is TextInputEditTextNFormView)
            Assert.assertTrue(verticalRootView.getChildAt(1) is TextInputEditTextNFormView)
            Assert.assertTrue(verticalRootView.getChildAt(3) is CheckBoxNFormView)
            Assert.assertTrue(verticalRootView.getChildAt(4) is SpinnerNFormView)
            Assert.assertTrue(verticalRootView.getChildAt(5) is MultiChoiceCheckBox)
            Assert.assertTrue(verticalRootView.getChildAt(7) is RadioGroupView)
            Assert.assertTrue(verticalRootView.getChildAt(9) is DateTimePickerNFormView)
            val datePickerAttributes =
                (verticalRootView.getChildAt(9) as DateTimePickerNFormView).viewProperties.viewAttributes as Map<*, *>
            Assert.assertTrue(datePickerAttributes.containsKey("type") && datePickerAttributes["type"] == "date_picker")
            Assert.assertTrue(verticalRootView.getChildAt(10) is DateTimePickerNFormView)
            val timePickerAttributes =
                (verticalRootView.getChildAt(10) as DateTimePickerNFormView).viewProperties.viewAttributes as Map<*, *>
            Assert.assertTrue(timePickerAttributes.containsKey("type") && timePickerAttributes["type"] == "time_picker")
            Assert.assertTrue(verticalRootView.getChildAt(11) is NumberSelectorNFormView)
            Assert.assertTrue(verticalRootView.getChildAt(13) is NotificationNFormView)
        }

    @Test
    fun `Should parse json from json string, create views and register form rules`() =
        coroutinesTestRule.runBlockingTest {
            jsonFormBuilder = spyk(
                JsonFormBuilder(TestConstants.SAMPLE_JSON.trimIndent(), activity.get())
            )
            jsonFormBuilder.defaultContextProvider = coroutinesTestRule.testDispatcherProvider
            jsonFormEmbedded = JsonFormEmbedded(jsonFormBuilder, mainLayout).buildForm()
            Assert.assertNotNull(jsonFormBuilder.form)
            Assert.assertTrue(jsonFormBuilder.form?.steps?.size == 1)
            Assert.assertTrue(jsonFormBuilder.form?.steps?.get(0)?.stepName == "Demographics")

            //Main layout has on element: VerticalRootView inside a ScrollView
            Assert.assertTrue(mainLayout.childCount == 1)
            Assert.assertTrue(mainLayout.getChildAt(0) is ScrollView)
            val scrollView = mainLayout.getChildAt(0) as ScrollView
            //VerticalRootView has 3 EditTextNFormView
            val verticalRootView = scrollView.getChildAt(0) as VerticalRootView
            Assert.assertTrue(verticalRootView.childCount == 14)
            Assert.assertTrue(verticalRootView.getChildAt(0) is TextInputEditTextNFormView)
            Assert.assertTrue(verticalRootView.getChildAt(1) is TextInputEditTextNFormView)
            Assert.assertTrue(verticalRootView.getChildAt(3) is CheckBoxNFormView)
            Assert.assertTrue(verticalRootView.getChildAt(4) is SpinnerNFormView)
            Assert.assertTrue(verticalRootView.getChildAt(6) is MultiChoiceCheckBox)
            Assert.assertTrue(verticalRootView.getChildAt(7) is EditTextNFormView)
            Assert.assertTrue(verticalRootView.getChildAt(8) is RadioGroupView)
            val datePickerAttributes =
                (verticalRootView.getChildAt(10) as DateTimePickerNFormView).viewProperties.viewAttributes as Map<*, *>
            Assert.assertTrue(datePickerAttributes.containsKey("type") && datePickerAttributes["type"] == "date_picker")
            val timePickerAttributes =
                (verticalRootView.getChildAt(11) as DateTimePickerNFormView).viewProperties.viewAttributes as Map<*, *>
            Assert.assertTrue(timePickerAttributes.containsKey("type") && timePickerAttributes["type"] == "time_picker")
            Assert.assertTrue(verticalRootView.getChildAt(12) is NumberSelectorNFormView)
        }

    @Test
    fun `Should parse json from file source, update views from provided layout view with form rules`() =
        coroutinesTestRule.runBlockingTest {
            jsonFormBuilder = spyk(
                JsonFormBuilder(activity.get(), TestConstants.SAMPLE_ONE_FORM_FILE)
            )
            jsonFormBuilder.defaultContextProvider = coroutinesTestRule.testDispatcherProvider
            val inflater =
                activity.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.sample_custom_form_layout, null)
            val viewsList = listOf<View>(view)
            jsonFormEmbedded = JsonFormEmbedded(jsonFormBuilder, mainLayout).buildForm(viewsList)
            Assert.assertTrue(mainLayout.getChildAt(0) is ScrollView)
            val scrollView = mainLayout.getChildAt(0) as ScrollView
            val verticalRootView = scrollView.getChildAt(0) as VerticalRootView
            Assert.assertTrue(verticalRootView.childCount == 1)
            Assert.assertTrue((verticalRootView.getChildAt(0) as ConstraintLayout).getChildAt(4) is EditTextNFormView)

            val editTextAttributes =
                ((verticalRootView.getChildAt(0) as ConstraintLayout).getChildAt(4) as EditTextNFormView).viewProperties.viewAttributes as Map<*, *>

            Assert.assertTrue(editTextAttributes.containsKey("hint") && editTextAttributes["hint"] == "Specify your language")
        }

    @Test
    fun `Should build default form (in vertical layout) using stepper library`() =
        coroutinesTestRule.runBlockingTest {
            jsonFormBuilder = spyk(
                objToCopy =
                JsonFormBuilder(activity.get(), TestConstants.SAMPLE_TWO_FORM_FILE),
                recordPrivateCalls = true
            )
            jsonFormBuilder.defaultContextProvider = coroutinesTestRule.testDispatcherProvider
            val stepperModel = StepperModel.Builder()
                .toolbarColorResource(R.color.colorBlack)
                .build()
            neatStepperLayout.stepperModel = stepperModel
            jsonFormStepper = JsonFormStepper(jsonFormBuilder, neatStepperLayout).buildForm()
            Assert.assertNotNull(jsonFormBuilder.form)
            Assert.assertNotNull(neatStepperLayout.stepperModel.toolbarColorResId == R.color.colorBlack)
            Assert.assertTrue(neatStepperLayout.findViewById<TextView>(R.id.titleTextView).text.toString() == "Profile")
            val innerStepperLayout = neatStepperLayout.getChildAt(0) as LinearLayout
            //Stepper has toolbar, frameLayout (fragment content) and bottom navigation
            Assert.assertTrue(innerStepperLayout.childCount == 3)

            //Get view pager inside the frameLayout and inspect its content
            val stepsViewPager =
                (innerStepperLayout.getChildAt(1) as FrameLayout).getChildAt(2) as ViewPager
            Assert.assertTrue(stepsViewPager.adapter is StepperPagerAdapter)
            val stepperPageAdapter = stepsViewPager.adapter as StepperPagerAdapter

            //There are only 2 steps for the form
            Assert.assertTrue(stepperPageAdapter.fragmentList.size == 2)

            //Test step one - check first and last view on the passed fragment view argument
            val stepOneView = stepperPageAdapter.fragmentList[0] as StepFragment
            Assert.assertNotNull(stepOneView)
            Assert.assertNotNull(stepOneView.arguments)
            val stepOneVerticalRootView =
                stepOneView.arguments?.get(FRAGMENT_VIEW) as VerticalRootView
            Assert.assertTrue(stepOneVerticalRootView.childCount == 13)
            Assert.assertTrue(stepOneVerticalRootView.getChildAt(0) is TextInputEditTextNFormView)
            Assert.assertTrue(stepOneVerticalRootView.getChildAt(12) is MultiChoiceCheckBox)

            //Test step two - only has one item on the passed fragment view argument
            val stepTwoView = stepperPageAdapter.fragmentList[1] as StepFragment
            Assert.assertNotNull(stepTwoView)
            Assert.assertNotNull(stepTwoView.arguments)
            val stepTwoVerticalRootView =
                stepTwoView.arguments?.get(FRAGMENT_VIEW) as VerticalRootView
            Assert.assertTrue(stepTwoVerticalRootView.childCount == 1)
            Assert.assertTrue(stepTwoVerticalRootView.getChildAt(0) is TextInputEditTextNFormView)
        }

    @Test
    fun `Should build customized form (using provided layout) using stepper library`() {
        coroutinesTestRule.runBlockingTest {

            jsonFormBuilder = spyk(
                objToCopy =
                JsonFormBuilder(activity.get(), TestConstants.SAMPLE_ONE_FORM_FILE),
                recordPrivateCalls = true
            )
            jsonFormBuilder.defaultContextProvider = coroutinesTestRule.testDispatcherProvider
            val inflater =
                activity.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.sample_custom_form_layout, null)
            val viewsList = listOf<View>(view)
            val stepperModel = StepperModel.Builder()
                .toolbarColorResource(R.color.colorBlack)
                .build()
            neatStepperLayout.stepperModel = stepperModel
            jsonFormStepper = JsonFormStepper(jsonFormBuilder, neatStepperLayout).buildForm(viewsList)

            Assert.assertNotNull(jsonFormBuilder.form)
            Assert.assertNotNull(neatStepperLayout.stepperModel.toolbarColorResId == R.color.colorBlack)
            Assert.assertTrue(neatStepperLayout.findViewById<TextView>(R.id.titleTextView).text.toString() == "Profile")
            val innerStepperLayout = neatStepperLayout.getChildAt(0) as LinearLayout
            //Stepper has toolbar, frameLayout (fragment content) and bottom navigation
            Assert.assertTrue(innerStepperLayout.childCount == 3)

            //Get view pager inside the frameLayout and inspect its content
            val stepsViewPager =
                (innerStepperLayout.getChildAt(1) as FrameLayout).getChildAt(2) as ViewPager
            Assert.assertTrue(stepsViewPager.adapter is StepperPagerAdapter)
            val stepperPageAdapter = stepsViewPager.adapter as StepperPagerAdapter

            //There is only one step in the form
            Assert.assertTrue(stepperPageAdapter.fragmentList.size == 1)

            val stepOneView = stepperPageAdapter.fragmentList[0] as StepFragment
            Assert.assertNotNull(stepOneView)
            Assert.assertNotNull(stepOneView.arguments)
            val stepOneVerticalRootView =
                stepOneView.arguments?.get(FRAGMENT_VIEW) as VerticalRootView

            Assert.assertNotNull(stepOneVerticalRootView)
            Assert.assertTrue(stepOneVerticalRootView.childCount == 1)
            Assert.assertTrue(
                (stepOneVerticalRootView.getChildAt(0) as ConstraintLayout).getChildAt(4) is EditTextNFormView
            )

            val editTextAttributes =
                ((stepOneVerticalRootView.getChildAt(0) as ConstraintLayout).getChildAt(4) as EditTextNFormView).viewProperties.viewAttributes as Map<*, *>

            Assert.assertTrue(editTextAttributes.containsKey("hint") && editTextAttributes["hint"] == "Specify your language")
        }
    }

    @Test
    fun `Should build a pre-filled form`() {
        coroutinesTestRule.runBlockingTest {
            jsonFormBuilder = spyk(
                JsonFormBuilder(activity.get(), TestConstants.SAMPLE_ONE_FORM_FILE)
            )
            jsonFormBuilder.defaultContextProvider = coroutinesTestRule.testDispatcherProvider
            jsonFormBuilder
                .withFormData(previousFormData, mutableSetOf("age", "child", "adult"))
            jsonFormEmbedded = JsonFormEmbedded(jsonFormBuilder, mainLayout).buildForm()
            Assert.assertNotNull(jsonFormBuilder.form)
            Assert.assertNotNull(jsonFormBuilder.formDataJson)
            val details = jsonFormBuilder.dataViewModel.details
            details.observeForever(observer)
            verify { observer.onChanged(any()) }
            Assert.assertEquals((details.value?.get("age") as NFormViewData).value, "54")
            Assert.assertEquals((details.value?.get("child") as NFormViewData).value, "yes")
            Assert.assertEquals((details.value?.get("adult") as NFormViewData).value, "0723721920")
        }
    }
}