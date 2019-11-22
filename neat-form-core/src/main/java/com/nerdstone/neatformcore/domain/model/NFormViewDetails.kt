package com.nerdstone.neatformcore.domain.model

import android.view.View

import java.io.Serializable

data class NFormViewDetails(val view: View) : Serializable {
    lateinit var name: String
    var value: Any? = null
    var metadata: Map<String, Any>? = null
    var subjects: List<String>? = null
}
