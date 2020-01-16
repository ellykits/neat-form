package com.nerdstone.neatformcore.domain.model

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class NForm : Serializable {

    @SerializedName("form")
    lateinit var formName: String

    @SerializedName("form_version")
    var formVersion: String? = null

    @SerializedName("rules_file")
    var rulesFile: String? = null

    @SerializedName("steps")
    lateinit var steps: List<NFormContent>

    @SerializedName("meta_data")
    val formMetadata: Map<String, Any>? = null

}

class NFormContent : Serializable {

    @SerializedName("title")
    var stepName: String? = null

    @SerializedName("fields")
    lateinit var fields: List<NFormViewProperty>

}

class NFormViewProperty : Serializable {

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("type")
    lateinit var type: String

    @SerializedName("properties")
    var viewAttributes: MutableMap<String, Any>? = null

    @SerializedName("meta_data")
    var viewMetadata: Map<String, Any>? = null

    @SerializedName("options")
    var options: List<NFormSubViewProperty>? = null

    @SerializedName("required_status")
    var requiredStatus: String? = null

    @SerializedName("validation")
    var validations: List<NFormFieldValidation>? = null

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

class NFormSubViewProperty : Serializable {

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("text")
    lateinit var text: String

    @SerializedName("is_exclusive")
    var isExclusive: Boolean? = null

    @SerializedName("meta_data")
    var viewMetadata: Map<String, Any>? = null

    @SerializedName("properties")
    var viewAttributes: Map<String, Any>? = null
}

class NFormFieldValidation : Serializable {

    @SerializedName("condition")
    var condition: String? = null

    @SerializedName("message")
    var message: String? = null
}