package com.nerdstone.neatform;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import com.nerdstone.neatformcore.datasource.AssetFile;
import com.nerdstone.neatformcore.domain.builders.FormBuilder;
import com.nerdstone.neatformcore.form.json.JsonFormBuilder;
import com.nerdstone.neatformcore.rules.RulesFactory.RulesFileType;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class MainActivityJava extends AppCompatActivity {

  private CompositeDisposable compositeDisposable;
  private LinearLayout mainLayout;
  private FormBuilder formBuilder;
  private String form;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    compositeDisposable = new CompositeDisposable();
    mainLayout = findViewById(R.id.mainLinearLayout);
    formBuilder = new JsonFormBuilder(mainLayout);

    new AssetFile()
        .readAssetFileAsString(this, "sample/sample_one_form.json").subscribe(
        new SingleObserver<String>() {
          @Override
          public void onSubscribe(Disposable d) {
            compositeDisposable.add(d);
          }

          @Override
          public void onSuccess(String s) {
            form = s;
          }

          @Override
          public void onError(Throwable e) {
            Timber.e("Error reading asset files: $err");
          }
        });

    formBuilder.getForm(form);
    formBuilder.createFormViews(this);
    formBuilder.registerFormRules(this, RulesFileType.YAML);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    compositeDisposable.addAll(((JsonFormBuilder) formBuilder).getCompositeDisposable());
    compositeDisposable.clear();

  }
}
