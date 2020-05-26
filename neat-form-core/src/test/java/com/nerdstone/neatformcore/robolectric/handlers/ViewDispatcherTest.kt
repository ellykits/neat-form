package com.nerdstone.neatformcore.robolectric.handlers

import com.nerdstone.neatformcore.TestNeatFormApp
import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.robolectric.builders.BaseJsonViewBuilderTest
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestNeatFormApp::class)
class ViewDispatcherTest : BaseJsonViewBuilderTest(){

    private val viewProperty = NFormViewProperty()
    private val rulesFactory = RulesFactory()
    private val editTextNFormView = EditTextNFormView(activity.get())

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "name"
        viewProperty.type = "edit_text"
        viewProperty.viewAttributes = hashMapOf("hint" to "Am a sample hint on field")
        editTextNFormView.viewProperties = viewProperty
        editTextNFormView.formValidator = this.formValidator
        every { formBuilder.viewDispatcher.rulesFactory } returns rulesFactory
    }

    @Test
    fun `Verify that field value is passed to the dispatcher`() {
        ViewUtils.setupView(editTextNFormView, viewProperty, formBuilder)
        editTextNFormView.setText("Sample text")
        verifyOrder {
           formBuilder. viewDispatcher.onPassData(editTextNFormView.viewDetails)
        }
        confirmVerified(formBuilder)
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}