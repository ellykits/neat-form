package com.nerdstone.neatformcore.robolectric.handlers

import androidx.appcompat.app.AppCompatActivity
import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.robolectric.builders.BaseJsonViewBuilderTest
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verifyOrder
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class `Test View Dispacher action` : BaseJsonViewBuilderTest(){

    private val viewProperty = NFormViewProperty()
    private val viewDispatcher = spyk<ViewDispatcher>()
    private val rulesFactory = RulesFactory.INSTANCE
    private val activity = Robolectric.buildActivity(AppCompatActivity::class.java).setup()
    private val editTextNFormView = EditTextNFormView(activity.get())

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "name"
        viewProperty.type = "edit_text"
        viewProperty.viewAttributes = hashMapOf("hint" to "Am a sample hint on field")
        editTextNFormView.viewProperties = viewProperty
        editTextNFormView.formValidator = this.formValidator
        every { viewDispatcher.rulesFactory } returns rulesFactory
        editTextNFormView.initView(viewProperty, viewDispatcher)
    }

    @Test
    fun `Verify that field value is passed to the dispatcher`() {
        editTextNFormView.setText("Sample text")
        verifyOrder {
            viewDispatcher.onPassData(editTextNFormView.viewDetails)
        }
        confirmVerified(viewDispatcher)
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}