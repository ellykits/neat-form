package com.nerdstone.neatformcore.domain.model

import com.nerdstone.neatandroidstepper.core.domain.StepperActions
import com.nerdstone.neatandroidstepper.core.model.StepperModel

class JsonFormBuilderModel private constructor() {
    var stepperModel: StepperModel? = null
    var stepperActions: StepperActions? = null

    data class Builder(
        var stepperModel: StepperModel? = null, var stepperActions: StepperActions? = null
    ) {

        fun build(): JsonFormBuilderModel {
            return JsonFormBuilderModel().also {
                it.stepperModel = stepperModel
                it.stepperActions = stepperActions
            }
        }
    }
}