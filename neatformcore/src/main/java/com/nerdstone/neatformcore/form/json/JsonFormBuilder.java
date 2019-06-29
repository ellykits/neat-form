package com.nerdstone.neatformcore.form.json;

import static com.nerdstone.neatformcore.rx.RxHelper.getSingleIoScheduler;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.nerdstone.neatformcore.domain.model.NForm;
import com.nerdstone.neatformcore.domain.model.NFormContent;
import com.nerdstone.neatformcore.domain.view.FormBuilder;
import com.nerdstone.neatformcore.domain.view.RootView;
import com.nerdstone.neatformcore.rules.RulesFactory;
import com.nerdstone.neatformcore.views.containers.VerticalRootView;
import com.nerdstone.neatformcore.views.data.ViewDataHandler;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.jeasy.rules.api.Rules;
import timber.log.Timber;

/***
 * @author Elly Nerdstone
 *
 */
public class JsonFormBuilder implements FormBuilder {


  private static final String TAG = JsonFormBuilder.class.getCanonicalName();
  private ViewGroup mainLayout;
  private ViewDataHandler viewDataHandler;
  private NForm form;
  private CompositeDisposable compositeDisposable;
  private RulesFactory rulesFactory;

  public JsonFormBuilder(ViewGroup mainLayout) {
    this.mainLayout = mainLayout;
    viewDataHandler = ViewDataHandler.getInstance();
    compositeDisposable = new CompositeDisposable();
    rulesFactory = RulesFactory.getInstance();
  }

  @Override
  public ViewGroup getMainLayout() {
    return this.mainLayout;
  }

  @Override
  public void setMainLayout(ViewGroup mainLayout) {
    this.mainLayout = mainLayout;
  }

  @Override
  public NForm getForm(String source) {
    if (form == null) {
      form = JsonFormParser.parseJson(source);
    }
    return form;
  }

  /***
   * @param context android context
   */
  @Override
  public void createFormViews(Context context) {
    if (form != null && form.getSteps() != null) {
      for (NFormContent formContent : form.getSteps()) {
        RootView rootView = new VerticalRootView(context);
        rootView.addChildren(formContent.getFields(), viewDataHandler);
        mainLayout.addView((View) rootView.initRootView());
      }
    }
    registerFormRules(context);
  }

  @Override
  public void setViewDataHandler(ViewDataHandler viewDataHandler) {
    this.viewDataHandler = viewDataHandler;
  }

  @Override
  public void registerFormRules(Context context) {
    rulesFactory.readRulesFromFile(context, form.getRulesFile())
        .compose(getSingleIoScheduler())
        .subscribe(
            new SingleObserver<Rules>() {
              @Override
              public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
              }

              @Override
              public void onSuccess(Rules rules) {
              }

              @Override
              public void onError(Throwable e) {
                Timber.tag(TAG).e(e);
              }
            }
        );
  }

  @Override
  public void freeResources() {
    compositeDisposable.clear();
  }
}
