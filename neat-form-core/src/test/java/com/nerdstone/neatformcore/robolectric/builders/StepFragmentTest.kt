package com.nerdstone.neatformcore.robolectric.builders

import androidx.fragment.app.FragmentActivity
import com.nerdstone.neatandroidstepper.core.model.StepModel
import com.nerdstone.neatformcore.StepperTestActivity
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.form.json.FRAGMENT_INDEX
import com.nerdstone.neatformcore.form.json.FRAGMENT_VIEW
import com.nerdstone.neatformcore.form.json.StepFragment
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class StepFragmentTest {

    @Test
    fun `Should create a step fragment with the provided view and index`() {
        val activity =
            Robolectric.buildActivity(StepperTestActivity::class.java)
                .create()
                .start()
                .resume()
                .get()
        val verticalRootView = VerticalRootView(activity)
        val stepFragment =
            StepFragment.newInstance(0, StepModel.Builder().build(), verticalRootView)
        activity.supportFragmentManager.beginTransaction()
            .add(stepFragment, StepFragment::class.simpleName)
            .commitNow()
        Assert.assertNotNull(stepFragment.activity)
        Assert.assertEquals(stepFragment.arguments?.get(FRAGMENT_INDEX), 0)
        Assert.assertEquals(stepFragment.arguments?.get(FRAGMENT_VIEW), verticalRootView)
    }

    @Test(expected = ClassCastException::class)
    fun `Should throw an error if activity does not implement FormActions interface`() {
        val activity =
            Robolectric.buildActivity(FragmentActivity::class.java)
                .create()
                .start()
                .resume()
                .get()
        val stepFragment =
            StepFragment.newInstance(0, StepModel.Builder().build(), VerticalRootView(activity))
        activity.supportFragmentManager.beginTransaction()
            .add(stepFragment, StepFragment::class.simpleName)
            .commitNow()
    }
}