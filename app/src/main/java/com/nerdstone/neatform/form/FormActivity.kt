package com.nerdstone.neatform.form

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.nerdstone.neatform.R
import com.nerdstone.neatformcore.datasource.AssetFile
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.rules.RulesFactory.RulesFileType
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber


class FormActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()
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

        formBuilder = JsonFormBuilder(mainLayout)

        if (intent.extras != null && intent.extras.getString("path") != null) {
            val path = intent.extras.getString("path")
            val pageTitle = intent.extras.getString("page").capitalizeWords()

            pageTitleTextView.text = pageTitle
            compositeDisposable.add(AssetFile()
                .readAssetFileAsString(this, path)
                .subscribe(
                    { file ->
                        formBuilder?.also {
                            it.getForm(file)
                            it.createFormViews(this)
                            it.registerFormRules(this, RulesFileType.YAML)
                        }

                    },
                    { err -> Timber.e("Error reading asset files: $err") }
                ))

            exitFormImageView.setOnClickListener {
                if (it.id == R.id.exitFormImageView) {
                    finish()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.addAll((formBuilder as JsonFormBuilder).compositeDisposable)
        compositeDisposable.clear()

    }
}