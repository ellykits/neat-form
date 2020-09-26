package com.nerdstone.neatform

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nerdstone.neatandroidstepper.core.model.StepperModel
import com.nerdstone.neatandroidstepper.core.stepper.Step
import com.nerdstone.neatandroidstepper.core.stepper.StepVerificationState
import com.nerdstone.neatform.form.FILE_PATH
import com.nerdstone.neatform.form.PRE_FILLED
import com.nerdstone.neatform.utils.Constants
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.form.common.FormActions
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.form.json.JsonFormStepper
import com.nerdstone.neatformcore.utils.createAlertDialog
import com.nerdstone.neatformcore.utils.populateResourceMap
import kotlinx.android.synthetic.main.activity_stepper.*
import timber.log.Timber

class StepperActivity : AppCompatActivity(), FormActions {

    override lateinit var formBuilder: FormBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stepper)
        val filePath = intent?.extras?.getString(FILE_PATH)
        val preFilled = intent?.extras?.getBoolean(PRE_FILLED, false)

        if (filePath == null) throw NullPointerException("Filepath cannot be null")

        neatStepperLayout.apply {
            stepperActions = this@StepperActivity
            stepperModel = StepperModel.Builder()
                    .exitButtonDrawableResource(R.drawable.ic_clear)
                    .indicatorType(StepperModel.IndicatorType.DOT_INDICATOR)
                    .toolbarColorResource(R.color.colorPrimaryDark)
                    .build()
        }
        formBuilder = JsonFormBuilder(this, filePath)
        formBuilder.also {
            it.populateResourceMap(R.drawable::class.java)
            it.populateResourceMap(R.style::class.java)
            if (preFilled!!) it.withFormData(Constants.PREVIOUS_DATA, mutableSetOf())
        }
        JsonFormStepper(formBuilder as JsonFormBuilder, neatStepperLayout).buildForm()
    }

    override fun onStepError(stepVerificationState: StepVerificationState) = Unit

    override fun onButtonNextClick(step: Step) = Unit

    override fun onButtonPreviousClick(step: Step) = Unit

    override fun onStepComplete(step: Step) {
        if (formBuilder.getFormDataAsJson() != "") {
            Toast.makeText(formBuilder.context, "Completed entire step", Toast.LENGTH_LONG)
                    .show()
            Timber.d("Saved Data = %s", formBuilder.getFormDataAsJson())
            finish()
        }
    }

    override fun onExitStepper() {
        formBuilder.context.createAlertDialog(
                title = "Confirm close",
                message = "All the unsaved data will get lost if you quit"
        ).apply {
            setPositiveButton("Exit") { _, _ -> finish() }
            setNegativeButton("Cancel") { _, _ -> return@setNegativeButton }
            create()
        }.show()
    }

    override fun onCompleteStepper() {
        if (formBuilder.getFormDataAsJson() != "") {
            Toast.makeText(formBuilder.context, "Completed entire step", Toast.LENGTH_LONG)
                    .show()
            Timber.d("Saved Data = %s", formBuilder.getFormDataAsJson())
            finish()
        }
    }
}
