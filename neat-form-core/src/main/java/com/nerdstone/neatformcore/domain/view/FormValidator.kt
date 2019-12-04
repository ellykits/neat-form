package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.builders.FormBuilder

interface FormValidator {

    var formBuilder: FormBuilder

    fun validateField(nFormView: NFormView): Pair<Boolean, String?>

    fun validateLabeledField(nFormView: NFormView): Boolean

}