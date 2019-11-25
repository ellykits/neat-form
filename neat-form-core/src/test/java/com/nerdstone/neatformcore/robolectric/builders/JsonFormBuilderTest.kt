package com.nerdstone.neatformcore.robolectric.builders

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.TestConstants
import com.nerdstone.neatformcore.TestCoroutineContextProvider
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import com.nerdstone.neatformcore.views.containers.RadioGroupView
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import com.nerdstone.neatformcore.views.widgets.*
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class `Test building form with JSON` {

    private val activity = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
    private val mainLayout: LinearLayout = LinearLayout(activity.get())
    private val jsonFormBuilder: JsonFormBuilder =
            spyk(JsonFormBuilder(mainLayout, TestConstants.SAMPLE_ONE_FORM_FILE))
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun `Before everything else`() {
        Dispatchers.setMain(testDispatcher)
        jsonFormBuilder.coroutineContextProvider = TestCoroutineContextProvider()
    }

    @Test
    fun `Should parse json, create views and register form rules`() =
            runBlockingTest {
                jsonFormBuilder.buildForm(null)
                Assert.assertNotNull(jsonFormBuilder.form)
                Assert.assertTrue(jsonFormBuilder.form?.steps?.size == 1)
                Assert.assertTrue(jsonFormBuilder.form?.steps?.get(0)?.stepName == "Test and counselling")

                //Main layout has on element: VerticalRootView
                Assert.assertTrue(mainLayout.childCount == 1)
                Assert.assertTrue(mainLayout.getChildAt(0) is VerticalRootView)
                //VerticalRootView has 3 EditTextNFormView
                val verticalRootView = mainLayout.getChildAt(0) as VerticalRootView
                Assert.assertTrue(verticalRootView.childCount == 13)
                Assert.assertTrue(verticalRootView.getChildAt(0) is EditTextNFormView)
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
            }

    @Test
    fun `Should parse json, update views from provided layout view with form rules`() =
            runBlockingTest {

                val inflater = ApplicationProvider.getApplicationContext<Context>().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

                val view = inflater.inflate(R.layout.sample_custom_form_layout, null);
                val viewsList = listOf<View>(view)

                jsonFormBuilder.buildForm(null,viewsList)

                val verticalRootView = mainLayout.getChildAt(0) as VerticalRootView
                Assert.assertTrue(verticalRootView.childCount == 1)
                Assert.assertTrue((verticalRootView.getChildAt(0) as ConstraintLayout).getChildAt(4) is EditTextNFormView)

                val editTextAttributes =
                        ((verticalRootView.getChildAt(0) as ConstraintLayout).getChildAt(4) as EditTextNFormView).viewProperties.viewAttributes as Map<*, *>

                Assert.assertTrue(editTextAttributes.containsKey("hint") && editTextAttributes["hint"] == "Specify your language")
            }


    @After
    fun `Tearing everything down`() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}