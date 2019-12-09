package com.nerdstone.neatformcore.robolectric.builders

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.nerdstone.neatformcore.TestConstants
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.rules.NeatFormValidator
import org.robolectric.Robolectric
import org.robolectric.android.controller.ActivityController


open class BaseJsonViewBuilderTest {
    protected val activity: ActivityController<AppCompatActivity> =
        Robolectric.buildActivity(AppCompatActivity::class.java).setup()
    private val mainLayout: ViewGroup = LinearLayout(activity.get())
    var formBuilder = JsonFormBuilder(TestConstants.SAMPLE_JSON, activity.get(), mainLayout)
    val formValidator = NeatFormValidator.INSTANCE

    init {
        formValidator.formBuilder = formBuilder
    }
}