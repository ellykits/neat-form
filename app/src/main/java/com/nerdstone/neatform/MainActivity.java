package com.nerdstone.neatform;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;

import com.nerdstone.neatformcore.datasource.AssetFile;
import com.nerdstone.neatformcore.domain.model.NForm;
import com.nerdstone.neatformcore.domain.view.FormBuilder;
import com.nerdstone.neatformcore.form.json.JsonFormBuilder;
import com.nerdstone.neatformcore.rx.RxHelper;

import io.reactivex.disposables.CompositeDisposable;


public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getCanonicalName();
    private CompositeDisposable compositeDisposable;
    private FormBuilder formBuilder;
    private NForm nForm;
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.mainLinearLayout);
        formBuilder = new JsonFormBuilder(mainLayout);
        compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(new AssetFile()
                .readAssetFileAsString(this, "sample/sample_one_form_json.json")
                .compose(RxHelper.getSingleIoScheduler())
                .subscribe(
                        file -> {
                            nForm = formBuilder.getForm(file);
                            formBuilder.addFormViews(nForm, this);
                        },
                        err -> Log.e(TAG, "Error reading asset files: " + err)
                ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
