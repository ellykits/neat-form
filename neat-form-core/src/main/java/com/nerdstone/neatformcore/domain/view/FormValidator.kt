package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.builders.FormBuilder

interface FormValidator {

    var formBuilder: FormBuilder

    val invalidFields: HashSet<String>

    val requiredFields: HashSet<String>

    fun validateField(nFormView: NFormView): Pair<Boolean, String?>

    fun validateLabeledField(nFormView: NFormView): Boolean

}