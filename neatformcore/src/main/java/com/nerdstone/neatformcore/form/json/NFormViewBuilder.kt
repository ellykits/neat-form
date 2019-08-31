package com.nerdstone.neatformcore.form.json

import android.view.View

import com.nerdstone.neatformcore.utils.Constants

object NFormViewBuilder {

    fun makeView(attributes: Map<String, Any>, view: View, type: String) {
        when (type) {
            Constants.ViewType.EDIT_TEXT -> makeEditText(attributes, view)
        }
    }

    private fun makeEditText(attributes: Map<String, Any>, view: View) {

    }

}
