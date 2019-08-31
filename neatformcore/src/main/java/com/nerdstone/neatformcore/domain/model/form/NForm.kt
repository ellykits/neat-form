package com.nerdstone.neatformcore.domain.model.form

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class NForm : Serializable {

    @SerializedName("form")
    lateinit var formName: String

    @SerializedName("is_multi_step")
    var isMultiStep: Boolean = false

    @SerializedName("rules_file")
    var rulesFile: String? = null

    @SerializedName("count")
    var count: Int = 0

    @SerializedName("steps")
    lateinit var steps: List<NFormContent>

    @SerializedName("meta_data")
    val formMetadata: Map<String, Any>? = null

}
