package com.nerdstone.neatformcore.robolectric.builders

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.nerdstone.neatformcore.TestConstants
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import kotlinx.coroutines.cancelChildren
import org.junit.After
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
abstract class BaseJsonViewBuilderTest {
    protected val activity: ActivityController<AppCompatActivity> =
        Robolectric.buildActivity(AppCompatActivity::class.java).setup()
    val mainLayout: ViewGroup = LinearLayout(activity.get())
    var formBuilder = JsonFormBuilder(TestConstants.SAMPLE_JSON, activity.get())

    @After
    fun `Clean resources`() {
        formBuilder.dataViewModel.viewModelScope.coroutineContext.cancelChildren()
        formBuilder.formViewModel.viewModelScope.coroutineContext.cancelChildren()
    }
}