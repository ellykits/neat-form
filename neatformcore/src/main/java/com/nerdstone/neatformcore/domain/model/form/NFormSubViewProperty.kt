package com.nerdstone.neatformcore.domain.model.form

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class NFormSubViewProperty : Serializable {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("label")
    var label: String? = null

    @SerializedName("is_exclusive")
    var isExclusive: Boolean? = null

    @SerializedName("metadata")
    var viewMetadata: Map<String, Any>? = null
}
