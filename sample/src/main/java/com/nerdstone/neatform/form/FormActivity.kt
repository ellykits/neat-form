package com.nerdstone.neatform.form

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.nerdstone.neatform.FormType
import com.nerdstone.neatform.R
import com.nerdstone.neatform.StepperActivity
import com.nerdstone.neatform.custom.views.CustomImageView
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.form.json.JsonFormEmbedded
import timber.log.Timber

const val FILE_PATH = "FILE_PATH"
const val PRE_FILLED = "PRE_FILLED"

class FormActivity : AppCompatActivity() {

    private lateinit var formLayout: LinearLayout
    private lateinit var mainLayout: LinearLayout
    private lateinit var sampleToolBar: Toolbar
    private lateinit var pageTitleTextView: TextView
    private lateinit var exitFormImageView: ImageView
    private lateinit var completeButton: ImageView
    private var formBuilder: FormBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_activity)

        mainLayout = findViewById(R.id.mainLayout)
        formLayout = findViewById(R.id.formLayout)
        sampleToolBar = findViewById(R.id.sampleToolBar)
        pageTitleTextView = findViewById(R.id.pageTitleTextView)
        exitFormImageView = findViewById(R.id.exitFormImageView)
        completeButton = findViewById(R.id.completeButton)

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
                FormType.jsonFromEmbeddedDefault -> {
                    formBuilder = JsonFormBuilder(this, formData.filePath)
                    formBuilder?.also {
                        it.registeredViews["custom_image"] = CustomImageView::class
                        JsonFormEmbedded(formBuilder as JsonFormBuilder, formLayout).buildForm()
                    }
                }
                FormType.jsonFormEmbeddedCustomized -> {
                    formBuilder = JsonFormBuilder(this, formData.filePath)
                    JsonFormEmbedded(formBuilder as JsonFormBuilder, formLayout).buildForm(views)
                }
                FormType.jsonFormStepperDefault -> {
                    startStepperActivity(formData.filePath)
                }
                FormType.jsonFormStepperCustomized -> {
                    startStepperActivity(formData.filePath)
                }
                else -> Toast.makeText(
                    this, "Please provide the right form type",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun startStepperActivity(filePath: String, preFilled: Boolean = false) {
        finish()
        startActivity(Intent(this, StepperActivity::class.java).apply {
            putExtra(FILE_PATH, filePath)
            putExtra(PRE_FILLED, preFilled)
        })
    }
}