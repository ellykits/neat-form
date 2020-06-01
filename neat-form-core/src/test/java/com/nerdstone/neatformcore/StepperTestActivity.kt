package com.nerdstone.neatformcore

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.nerdstone.neatandroidstepper.core.stepper.Step
import com.nerdstone.neatandroidstepper.core.stepper.StepVerificationState
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.form.common.FormActions
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import timber.log.Timber

class StepperTestActivity : FragmentActivity(), FormActions {

    override lateinit var formBuilder: FormBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        formBuilder = JsonFormBuilder(this, TestConstants.SAMPLE_JSON)
    }

    override fun onButtonNextClick(step: Step) {
        Timber.d("onButtonNextClick")
    }

    override fun onButtonPreviousClick(step: Step) {
        Timber.d("onButtonPreviousClick")
    }

    override fun onCompleteStepper() {
        Timber.d("onCompleteStepper")
    }

    override fun onExitStepper() {
        Timber.d("onExitStepper")
    }

    override fun onStepComplete(step: Step) {
        Timber.d("onStepComplete")
    }

    override fun onStepError(stepVerificationState: StepVerificationState) {
        Timber.d("onStepError")
    }
}