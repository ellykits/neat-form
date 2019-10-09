package com.nerdstone.neatformcore.robolectric.builders

import android.widget.LinearLayout
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.robolectric.utils.TestConstants
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.views.containers.MultiChoiceCheckBox
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import com.nerdstone.neatformcore.views.widgets.CheckBoxNFormView
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import com.nerdstone.neatformcore.views.widgets.SpinnerNFormView
import io.mockk.spyk
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class `Test building form with JSON` {

    private val mainLayout: LinearLayout = LinearLayout(RuntimeEnvironment.systemContext)
    private val jsonFormBuilder: JsonFormBuilder = spyk(JsonFormBuilder(mainLayout))

    @Before
    fun `Before everything else`() {
        jsonFormBuilder.getForm(TestConstants.SAMPLE_JSON)
    }

    @Test
    fun `Should read form from assets`() {
        Assert.assertNotNull(jsonFormBuilder.form)
        Assert.assertTrue(jsonFormBuilder.form?.steps?.size == 1)
        Assert.assertTrue(jsonFormBuilder.form?.steps?.get(0)?.stepName == "Test and counselling")
    }

    @Test
    fun `Should read rules from assets then free resources`() {
        jsonFormBuilder.registerFormRules(
            RuntimeEnvironment.systemContext,
            RulesFactory.RulesFileType.YAML
        )
        Assert.assertTrue(jsonFormBuilder.compositeDisposable.size() > 0)
        jsonFormBuilder.freeResources()
        Assert.assertTrue(jsonFormBuilder.compositeDisposable.isDisposed)
    }

    @Test
    fun `Should create views with the parsed form`() {
        jsonFormBuilder.createFormViews(RuntimeEnvironment.application)
        //Main layout has on element: VerticalRootView
        Assert.assertTrue(mainLayout.childCount == 1)
        Assert.assertTrue(mainLayout.getChildAt(0) is VerticalRootView)
        //VerticalRootView has 3 EditTextNFormView
        val verticalRootView = mainLayout.getChildAt(0) as VerticalRootView
        Assert.assertTrue(verticalRootView.childCount == 7)
        Assert.assertTrue(verticalRootView.getChildAt(0) is EditTextNFormView)
        Assert.assertTrue(verticalRootView.getChildAt(3) is CheckBoxNFormView)
        Assert.assertTrue(verticalRootView.getChildAt(4) is SpinnerNFormView)
        Assert.assertTrue(verticalRootView.getChildAt(5) is MultiChoiceCheckBox)
    }
}