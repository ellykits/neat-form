package com.nerdstone.neatformcore.domain.view

interface FormValidator {

    val invalidFields: HashSet<String>

    val requiredFields: HashSet<String>

    fun validateField(nFormView: NFormView): Pair<Boolean, String?>

    fun validateLabeledField(nFormView: NFormView): Boolean

}