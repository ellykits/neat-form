package com.nerdstone.neatformcore.robolectric.handlers

import com.nerdstone.neatformcore.domain.model.NFormViewProperty
import com.nerdstone.neatformcore.robolectric.builders.BaseJsonViewBuilderTest
import com.nerdstone.neatformcore.utils.setupView
import com.nerdstone.neatformcore.views.widgets.EditTextNFormView
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ViewDispatcherTest : BaseJsonViewBuilderTest() {

    private val viewProperty = NFormViewProperty()
    private val editTextNFormView = EditTextNFormView(activity.get())

    @Before
    fun `Before doing anything else`() {
        viewProperty.name = "name"
        viewProperty.type = "edit_text"
        viewProperty.viewAttributes = hashMapOf("hint" to "Am a sample hint on field")
        editTextNFormView.viewProperties = viewProperty
        editTextNFormView.setupView(viewProperty, formBuilder)
    }

    @Test
    fun `Verify that field value is passed to the dispatcher`() {
         val sample = "Sample text"
        editTextNFormView.setText(sample)
        Assert.assertEquals(editTextNFormView.viewDetails.value, sample)
    }

    @After
    fun `After everything else`() {
        unmockkAll()
    }
}