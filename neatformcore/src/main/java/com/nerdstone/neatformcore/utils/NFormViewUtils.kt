package com.nerdstone.neatformcore.utils

import android.content.Context
import com.nerdstone.neatformcore.domain.model.form.NFormViewProperty
import com.nerdstone.neatformcore.domain.view.RootView
import com.nerdstone.neatformcore.utils.Constants.ViewType
import com.nerdstone.neatformcore.views.controls.EditTextNFormView
import com.nerdstone.neatformcore.views.controls.SpinnerNFormView
import com.nerdstone.neatformcore.views.handlers.ViewDispatcher
import java.util.*

object NFormViewUtils {

    fun createViews(rootView: RootView, viewProperties: List<NFormViewProperty>,
                    context: Context, viewDispatcher: ViewDispatcher) {

        for (viewProperty in viewProperties) {
            registerSubjects(splitText(viewProperty.subjects, ","), viewProperty, viewDispatcher)
            when (viewProperty.type) {
                ViewType.EDIT_TEXT -> rootView.addChild(EditTextNFormView(context).initView(viewProperty, viewDispatcher))
                ViewType.SPINNER -> rootView.addChild(SpinnerNFormView(context).initView(viewProperty, viewDispatcher))
            }
        }
    }

    private fun registerSubjects(subjects: List<String>, viewProperty: NFormViewProperty,
                                 viewDispatcher: ViewDispatcher) {


    }

    fun splitText(text: String?, delimiter: String): List<String> {
        return if (text == null || text.isEmpty()) {
            ArrayList()
        } else Arrays.asList(*text.split(delimiter.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
    }

}
