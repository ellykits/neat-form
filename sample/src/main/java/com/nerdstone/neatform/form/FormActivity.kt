package com.nerdstone.neatform.form

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.nerdstone.neatform.R
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.form.json.JsonFormBuilder


class FormActivity : AppCompatActivity() {
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



        if (intent.extras != null) {
            val path = intent?.extras?.getString("path") ?: ""
            val pageTitle = intent?.extras?.getString("page")?.capitalizeWords()
            pageTitleTextView.text = pageTitle
            exitFormImageView.setOnClickListener {
                if (it.id == R.id.exitFormImageView) {
                    finish()
                }
            }

            formBuilder = if (pageTitle.equals("Programmer Survey"))
                JsonFormBuilder(mainLayout, path).buildForm()
            else {
                val views = listOf<View>(
                    layoutInflater.inflate(
                        R.layout.sample_one_form_custom_layout,
                        null
                    )
                )
                JsonFormBuilder(mainLayout, path).buildForm(views)
            }
        }
    }
}