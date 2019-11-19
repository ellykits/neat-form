package com.nerdstone.neatform.form

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

            if (pageTitle.equals("Programmer Survey"))
                formBuilder = JsonFormBuilder(mainLayout, path).buildForm(null)
            else
                formBuilder = JsonFormBuilder(mainLayout, path).buildForm(layoutInflater.inflate(R.layout.sample_one_form_custom_layout, null))
        }
    }
}