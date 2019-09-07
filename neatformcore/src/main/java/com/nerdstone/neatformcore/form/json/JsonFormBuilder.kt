package com.nerdstone.neatformcore.form.json

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.nerdstone.neatformcore.domain.builders.FormBuilder
import com.nerdstone.neatformcore.domain.model.NForm
import com.nerdstone.neatformcore.rules.RulesFactory
import com.nerdstone.neatformcore.rules.RulesFactory.RulesFileType
import com.nerdstone.neatformcore.views.containers.VerticalRootView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/***
 * @author Elly Nerdstone
 */
class JsonFormBuilder(override var mainLayout: ViewGroup) :
    FormBuilder {
    private var viewDispatcher: ViewDispatcher
    private var form: NForm? = null
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val rulesFactory: RulesFactory

    init {
        viewDispatcher = ViewDispatcher.INSTANCE
        rulesFactory = RulesFactory.INSTANCE
    }

    override fun getForm(source: String): NForm? {
        if (form == null) {
            form = JsonFormParser.parseJson(source)
        }
        return form
    }

    /***
     * @param context android context
     */
    override fun createFormViews(context: Context) {
        if (form != null) {
            for (formContent in form!!.steps) {
                val rootView = VerticalRootView(context)
                rootView.addChildren(formContent.fields, viewDispatcher)
                mainLayout.addView(rootView.initRootView() as View)
            }
        }
    }

    override fun setViewDispatcher(viewDispatcher: ViewDispatcher) {
        this.viewDispatcher = viewDispatcher
    }

    override fun registerFormRules(context: Context, rulesFileType: RulesFileType) {
        form?.rulesFile?.let {
            rulesFactory.readRulesFromFile(context, it, rulesFileType)
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                    object : CompletableObserver {
                        override fun onSubscribe(d: Disposable) {
                            compositeDisposable.add(d)
                        }

                        override fun onComplete() {
                            Timber.i("Completed reading rules from file successfully")
                        }

                        override fun onError(e: Throwable) {
                            Timber.e(e)
                        }
                    })
        }
    }

    override fun freeResources() {
        compositeDisposable.clear()
    }

}
