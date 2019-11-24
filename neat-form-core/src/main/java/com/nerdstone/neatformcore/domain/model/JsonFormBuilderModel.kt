package com.nerdstone.neatformcore.domain.model

import com.nerdstone.neatandroidstepper.core.domain.StepperActions
import com.nerdstone.neatandroidstepper.core.model.StepperModel

class JsonFormBuilderModel private constructor() {
    var stepperModel: StepperModel = StepperModel.Builder().build()
    var stepperActions: StepperActions? = null

    data class Builder(
        var stepperActions: StepperActions? = null,
        var stepperModel: StepperModel = StepperModel.Builder().build()
    ) {

        fun build(): JsonFormBuilderModel {
            return JsonFormBuilderModel().also {
                it.stepperModel = stepperModel
                it.stepperActions = stepperActions
            }
        }
    }
}