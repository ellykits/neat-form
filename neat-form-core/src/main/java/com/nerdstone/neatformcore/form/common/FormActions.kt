package com.nerdstone.neatformcore.form.common

import com.nerdstone.neatandroidstepper.core.domain.StepperActions
import com.nerdstone.neatformcore.domain.builders.FormBuilder

interface FormActions : StepperActions {
    var formBuilder: FormBuilder
}