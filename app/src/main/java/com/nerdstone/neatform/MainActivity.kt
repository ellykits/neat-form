package com.nerdstone.neatform

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.nerdstone.neatformcore.datasource.AssetFile
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.form.json.JsonFormBuilder
import com.nerdstone.neatformcore.rules.RulesFactory.RulesFileType
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var mainLayout: LinearLayout
    private var formBuilder: FormBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.mainLinearLayout)
        formBuilder = JsonFormBuilder(mainLayout)

        compositeDisposable.add(AssetFile()
            .readAssetFileAsString(this, "sample/sample_one_form.json")
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
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.addAll((formBuilder as JsonFormBuilder).compositeDisposable)
        compositeDisposable.clear()

    }
}
