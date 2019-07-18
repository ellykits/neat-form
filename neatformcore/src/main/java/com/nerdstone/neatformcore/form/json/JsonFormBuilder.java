package com.nerdstone.neatformcore.form.json;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.nerdstone.neatformcore.domain.model.form.NForm;
import com.nerdstone.neatformcore.domain.model.form.NFormContent;
import com.nerdstone.neatformcore.domain.view.FormBuilder;
import com.nerdstone.neatformcore.domain.view.RootView;
import com.nerdstone.neatformcore.rules.RulesFactory;
import com.nerdstone.neatformcore.rules.RulesFactory.RulesFileType;
import com.nerdstone.neatformcore.views.containers.VerticalRootView;
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.nerdstone.neatformcore.rx.RxHelper.getCompletableIoScheduler;

/***
 * @author Elly Nerdstone
 *
 */
public class JsonFormBuilder implements FormBuilder {

    private static final String TAG = JsonFormBuilder.class.getCanonicalName();
    private ViewGroup mainLayout;
    private ViewDispatcher viewDispatcher;
    private NForm form;
    private CompositeDisposable compositeDisposable;
    private RulesFactory rulesFactory;

    public JsonFormBuilder(ViewGroup mainLayout) {
        this.mainLayout = mainLayout;
        viewDispatcher = ViewDispatcher.getInstance();
        rulesFactory = RulesFactory.getInstance();
        compositeDisposable = new CompositeDisposable();
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
                rootView.addChildren(formContent.getFields(), viewDispatcher);
                mainLayout.addView((View) rootView.initRootView());
            }
        }
    }

    @Override
    public void setViewDispatcher(ViewDispatcher viewDispatcher) {
        this.viewDispatcher = viewDispatcher;
    }

    @Override
    public void registerFormRules(Context context, RulesFileType rulesFileType) {
        rulesFactory.readRulesFromFile(context, form.getRulesFile(), rulesFileType)
                .compose(getCompletableIoScheduler()).subscribe(
                new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Timber.tag(TAG).i("Completed reading rules from file successfully");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.tag(TAG).e(e);
                    }
                });
    }

    @Override
    public void freeResources() {
        compositeDisposable.clear();
    }
}
