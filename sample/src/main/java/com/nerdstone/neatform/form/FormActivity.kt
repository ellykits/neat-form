package com.nerdstone.neatform.form

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nerdstone.neatandroidstepper.core.domain.StepperActions
import com.nerdstone.neatandroidstepper.core.model.StepperModel
import com.nerdstone.neatandroidstepper.core.stepper.Step
import com.nerdstone.neatandroidstepper.core.stepper.StepVerificationState
import com.nerdstone.neatform.R
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.JsonFormStepBuilderModel
import com.nerdstone.neatformcore.form.json.JsonFormBuilder


class FormActivity : AppCompatActivity(), StepperActions {
    private lateinit var mainLayout: LinearLayout
    private lateinit var pageTitleTextView: TextView
    private lateinit var exitFormImageView: ImageView
    private var formBuilder: FormBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_activity)

        mainLayout = findViewById(R.id.mainLayout)
        pageTitleTextView = findViewById(R.id.pageTitleTextView)
        exitFormImageView = findViewById(R.id.exitFormImageView)

        val stepperModel = StepperModel.Builder()
            .exitButtonDrawableResource(R.drawable.ic_clear_white)
            .toolbarColorResource(R.color.colorPrimaryDark)
            .build()


        if (intent.extras != null) {
            val path = intent?.extras?.getString("path") ?: ""
            val pageTitle = intent?.extras?.getString("page")?.capitalizeWords()

            pageTitleTextView.text = pageTitle
            exitFormImageView.setOnClickListener {
                if (it.id == R.id.exitFormImageView) {
                    finish()
                }
            }


            formBuilder = when {
                pageTitle.equals("Default Forms") -> JsonFormBuilder(
                    this, path, mainLayout
                ).buildForm()
                pageTitle.equals("Default Forms with Stepper") -> {
                    JsonFormBuilder(this, path, null).buildForm(
                        JsonFormStepBuilderModel.Builder(this, stepperModel).build()
                    )
                }
                pageTitle.equals("Customized Forms") -> {
                    val views = listOf<View>(
                        layoutInflater.inflate(R.layout.sample_one_form_custom_layout, null)
                    )
                    JsonFormBuilder(this, path, mainLayout).buildForm(null, views)
                }
                else -> {
                    val views = listOf<View>(
                        layoutInflater.inflate(R.layout.sample_one_form_custom_layout, null)
                    )

                    JsonFormBuilder(this, path, mainLayout).buildForm(
                        JsonFormStepBuilderModel.Builder(this, stepperModel).build(),
                        views
                    )
                }
            }
        }
    }

    override fun onStepError(stepVerificationState: StepVerificationState) {
    }

    override fun onButtonNextClick(step: Step) {
    }

    override fun onButtonPreviousClick(step: Step) {
    }

    override fun onStepComplete(step: Step) {
        Toast.makeText(this, "Stepper completed", Toast.LENGTH_SHORT).show()
    }

    override fun onExitStepper() {
        val confirmCloseDialog = AlertDialog.Builder(this)
        confirmCloseDialog.apply {
            setTitle("Confirm close")
            setMessage("All the unsaved data will get lost if you quit")
            setPositiveButton("Exit") { _, _ -> finish() }
            setNegativeButton("Cancel") { _, _ -> return@setNegativeButton }
            create()
        }
        confirmCloseDialog.show()
    }

    override fun onCompleteStepper() {
        Toast.makeText(this, "Completed entire step", Toast.LENGTH_SHORT).show()
    }
}