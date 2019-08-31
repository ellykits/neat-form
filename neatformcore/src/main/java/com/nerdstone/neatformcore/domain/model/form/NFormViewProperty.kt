package com.nerdstone.neatformcore.domain.model.form

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class NFormViewProperty : Serializable {

    @SerializedName("name")
    var name: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("properties")
    var viewAttributes: Map<String, Any>? = null

    @SerializedName("meta_data")
    var viewMetadata: Map<String, Any>? = null

    @SerializedName("options")
    var options: List<NFormSubViewProperty>? = null

    @SerializedName("required_status")
    var requiredStatus: String? = null

    @SerializedName("validation")
    var validations: String? = null

    @SerializedName("subjects")
    var subjects: String? = null

    constructor()

    constructor(name: String) {
        this.name = name
    }

    constructor(name: String, type: String) {
        this.name = name
        this.type = type
    }
}
