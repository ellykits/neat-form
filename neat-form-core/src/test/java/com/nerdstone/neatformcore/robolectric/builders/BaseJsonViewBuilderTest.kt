package com.nerdstone.neatformcore.robolectric.builders

import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.form.json.JsonFormValidator
import io.mockk.spyk


open class BaseJsonViewBuilderTest {
    val formBuilder = spyk<JsonFormBuilder>()
    val formValidator = JsonFormValidator.getInstance(formBuilder)
}