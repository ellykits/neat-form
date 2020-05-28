package com.nerdstone.neatform

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.nerdstone.neatform.form.FormActivity
import com.nerdstone.neatform.form.FormData
import com.nerdstone.neatform.form.FormRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*

object FormType {
    const val jsonFromEmbeddedDefault = "JsonFormEmbedded - default"
    const val jsonFormEmbeddedCustomized = "JsonFormEmbedded - customised"
    const val jsonFormStepperDefault = "JsonFormStepper - default"
    const val jsonFormStepperCustomized = "JsonFormStepper - customised"
}

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var formRecyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var exitAppImageView: ImageView

    private var formRecyclerAdapter = FormRecyclerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        formRecyclerView = findViewById(R.id.formRecyclerView)
        floatingActionButton = findViewById(R.id.newFormFab)
        exitAppImageView = findViewById(R.id.exitAppImageView)
        formRecyclerView.layoutManager =
            LinearLayoutManager(this)
        formRecyclerAdapter.formList =
            mutableListOf(
                FormData(
                    formTitle = "TB Registration",
                    formCategory = FormType.jsonFromEmbeddedDefault,
                    filePath = "sample/tb_registration.json"
                ),
                FormData(
                    formTitle = "TB Followup",
                    formCategory = FormType.jsonFromEmbeddedDefault,
                    filePath = "sample/tb_followup_visit.json"
                ),
                FormData(
                    formTitle = "Customized Embedded Form",
                    formCategory = FormType.jsonFormEmbeddedCustomized,
                    filePath = "sample/sample_one_form.json"
                ),
                FormData(
                    formTitle = "Default Stepper Form",
                    formCategory = FormType.jsonFormStepperDefault,
                    filePath = "sample/sample_two_form.json"
                ),
                FormData(
                    formTitle = "Customized Stepper Form",
                    formCategory = FormType.jsonFormStepperCustomized,
                    filePath = "sample/sample_two_form.json"
                ),
                FormData(
                    formTitle = "Single Stepper Form",
                    formCategory = FormType.jsonFormStepperDefault,
                    filePath = "sample/sample_one_form.json"
                )
            )

        formRecyclerView.adapter = formRecyclerAdapter
        formRecyclerAdapter.listener = View.OnClickListener {
            val viewHolder = it.tag as RecyclerView.ViewHolder
            val formData = formRecyclerAdapter.formList[viewHolder.adapterPosition]
            val intent = Intent(this, FormActivity::class.java)
            intent.putExtra("formData", formData)
            startActivity(intent)
        }

        newFormFab.setOnClickListener(this)
        exitAppImageView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.newFormFab -> Snackbar.make(
                findViewById(R.id.mainActivityConstraintLayout),
                "Action not yet implemented",
                Snackbar.LENGTH_SHORT
            ).show()
            R.id.exitAppImageView -> finish()
        }
    }
}