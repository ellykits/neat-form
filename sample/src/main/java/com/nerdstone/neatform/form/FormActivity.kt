package com.nerdstone.neatform.form

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.nerdstone.neatandroidstepper.core.domain.StepperActions
import com.nerdstone.neatandroidstepper.core.model.StepperModel
import com.nerdstone.neatandroidstepper.core.stepper.Step
import com.nerdstone.neatandroidstepper.core.stepper.StepVerificationState
import com.nerdstone.neatform.FormType
import com.nerdstone.neatform.R
import com.nerdstone.neatform.custom.views.CustomImageView
import com.nerdstone.neatform.utils.replaceView
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.JsonFormStepBuilderModel
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.utils.DialogUtil
import timber.log.Timber

class FormActivity : AppCompatActivity(), StepperActions {

    private lateinit var formLayout: LinearLayout
    private lateinit var mainLayout: LinearLayout
    private lateinit var sampleToolBar: Toolbar
    private lateinit var pageTitleTextView: TextView
    private lateinit var exitFormImageView: ImageView
    private lateinit var completeButton: ImageView
    private var formBuilder: FormBuilder? = null

    private val previousFormData = """
        {
            "age": {
              "meta_data": {
                "openmrs_entity": "",
                "openmrs_entity_id": "",
                "openmrs_entity_parent": ""
              },
              "type": "TextInputEditTextNFormView",
              "value": "54"
            },
            "child": {
              "meta_data": {
                "openmrs_entity": "",
                "openmrs_entity_id": "",
                "openmrs_entity_parent": ""
              },
              "type": "TextInputEditTextNFormView",
              "value": "child"
            },
            "dob": {
              "type": "DateTimePickerNFormView",
              "value": 1589555422331
            },
            "time": {
              "type": "DateTimePickerNFormView",
              "value": 1589555422335
            },
            "adult": {
              "type": "TextInputEditTextNFormView",
              "value": "0723721920"
            },
            "email_subscription": {
              "type": "CheckBoxNFormView",
              "value": {
                "email_subscription": "Subscribe to email notifications"
              },
              "visible": true
            },
            "no_prev_pregnancies": {
              "type": "NumberSelectorNFormView",
              "value": 1,
              "visible": true
            },
            "gender": {
              "type": "SpinnerNFormView",
              "value": {
                "value": "Female"
              }
            },
            "country": {
              "type": "SpinnerNFormView",
              "value": {
                "meta_data": {
                  "country_code": "+61"
                },
                "value": "Australia",
                "visible": true
              },
              "visible": true
            },
            "choose_language": {
              "type": "MultiChoiceCheckBox",
              "value": {
                "kisw": {
                  "meta_data": {
                    "openmrs_entity": "",
                    "openmrs_entity_id": "A123123123123",
                    "openmrs_entity_parent": ""
                  },
                  "value": "Kiswahili",
                  "visible": true
                },
                "french": {
                  "meta_data": {
                    "openmrs_entity": "",
                    "openmrs_entity_id": "A123123123123",
                    "openmrs_entity_parent": ""
                  },
                  "value": "French",
                  "visible": true
                }
              },
              "visible": true
            },
            "wiki_contribution": {
              "type": "RadioGroupView",
              "value": {
                "yes": {
                  "value": "Yes",
                  "visible": true
                }
              },
              "visible": true
            }
        }
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_activity)

        mainLayout = findViewById(R.id.mainLayout)
        formLayout = findViewById(R.id.formLayout)
        sampleToolBar = findViewById(R.id.sampleToolBar)
        pageTitleTextView = findViewById(R.id.pageTitleTextView)
        exitFormImageView = findViewById(R.id.exitFormImageView)
        completeButton = findViewById(R.id.completeButton)

        val stepperModel = StepperModel.Builder()
            .exitButtonDrawableResource(R.drawable.ic_clear)
            .indicatorType(StepperModel.IndicatorType.DOT_INDICATOR)
            .toolbarColorResource(R.color.colorPrimaryDark)
            .build()


        if (intent.extras != null) {
            val formData = intent?.extras?.getSerializable("formData") as FormData
            pageTitleTextView.text = formData.formTitle
            exitFormImageView.setOnClickListener {
                if (it.id == R.id.exitFormImageView) {
                    finish()
                }
            }

            completeButton.setOnClickListener {
                if (it.id == R.id.completeButton) {
                    if (formBuilder?.getFormDataAsJson() != "") {
                        Toast.makeText(this, "Completed entire step", Toast.LENGTH_LONG).show()
                        Timber.d("Saved Data = %s", formBuilder?.getFormDataAsJson())
                        finish()
                    }
                }
            }

            val views = listOf<View>(
                layoutInflater.inflate(R.layout.sample_one_form_custom_layout, null)
            )
            when (formData.formCategory) {
                FormType.embeddableDefault -> {
                    formBuilder = JsonFormBuilder(this, formData.filePath, formLayout)
                    formBuilder?.also {
                        it.registeredViews["custom_image"] = CustomImageView::class
                        it.withFormData(
                            previousFormData, mutableSetOf(
                                "dob", "time", "email_subscription", "country",
                                "no_prev_pregnancies", "choose_language", "wiki_contribution"
                            )
                        ).buildForm()
                    }
                }
                FormType.embeddableCustomized -> {
                    formBuilder = JsonFormBuilder(this, formData.filePath, formLayout)
                    formBuilder?.also {
                        it.registeredViews["custom_image"] = CustomImageView::class
                        it.buildForm(viewList = views)
                    }
                }
                FormType.stepperDefault -> {
                    sampleToolBar.visibility = View.GONE
                    formBuilder = JsonFormBuilder(this, formData.filePath, null)
                    formBuilder?.also {
                        it.registeredViews["custom_image"] = CustomImageView::class
                        it.withFormData(previousFormData, mutableSetOf()).buildForm(
                            JsonFormStepBuilderModel.Builder(this, stepperModel).build()
                        )
                    }
                    replaceView(mainLayout, (formBuilder as JsonFormBuilder).neatStepperLayout)
                }
                FormType.stepperCustomized -> {
                    sampleToolBar.visibility = View.GONE
                    formBuilder = JsonFormBuilder(this, formData.filePath, formLayout)
                        .buildForm(
                            JsonFormStepBuilderModel.Builder(this, stepperModel).build(),
                            views
                        )
                    replaceView(mainLayout, (formBuilder as JsonFormBuilder).neatStepperLayout)
                }
                else -> Toast.makeText(
                    this, "Please provide the right form type",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onStepError(stepVerificationState: StepVerificationState) = Unit

    override fun onButtonNextClick(step: Step) = Unit

    override fun onButtonPreviousClick(step: Step) = Unit

    override fun onStepComplete(step: Step) {
        if (formBuilder?.getFormDataAsJson() != "") {
            Toast.makeText(this, "Completed entire step", Toast.LENGTH_LONG).show()
            Timber.d("Saved Data = %s", formBuilder?.getFormDataAsJson())
            finish()
        }
    }

    override fun onExitStepper() {
        DialogUtil.createAlertDialog(
            context = this, title = "Confirm close",
            message = "All the unsaved data will get lost if you quit"
        ).apply {
            setPositiveButton("Exit") { _, _ -> finish() }
            setNegativeButton("Cancel") { _, _ -> return@setNegativeButton }
            create()
        }.show()
    }

    override fun onCompleteStepper() {
        if (formBuilder?.getFormDataAsJson() != "") {
            Toast.makeText(this, "Completed entire step", Toast.LENGTH_LONG).show()
            Timber.d("Saved Data = %s", formBuilder?.getFormDataAsJson())
            finish()
        }
    }
}