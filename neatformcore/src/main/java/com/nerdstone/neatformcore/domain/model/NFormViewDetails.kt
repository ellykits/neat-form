package com.nerdstone.neatformcore.domain.model

import android.view.View

import java.io.Serializable

class NFormViewDetails : Serializable {

    var name: String? = null
    var value: Any? = null
    var view: View? = null
    var metadata: Map<String, Any>? = null
    var subjects: List<String>? = null

    constructor()

    constructor(view: View) {
        this.view = view
    }
}
