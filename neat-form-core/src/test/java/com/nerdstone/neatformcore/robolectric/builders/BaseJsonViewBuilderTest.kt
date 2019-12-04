package com.nerdstone.neatformcore.robolectric.builders

import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.nerdstone.neatformcore.TestConstants
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.form.json.JsonFormValidator
import org.robolectric.Robolectric


open class BaseJsonViewBuilderTest {
    private val activity = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
    private val mainLayout: ViewGroup = LinearLayout(activity.get())
    private val formBuilder = JsonFormBuilder(TestConstants.SAMPLE_JSON, activity.get(), mainLayout)
    val formValidator = JsonFormValidator(formBuilder)
}