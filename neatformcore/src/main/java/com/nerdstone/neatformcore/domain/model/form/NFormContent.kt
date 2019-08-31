package com.nerdstone.neatformcore.domain.model.form

import com.google.gson.annotations.SerializedName

import java.io.Serializable

class NFormContent : Serializable {

    @SerializedName("title")
    var stepName: String? = null

    @SerializedName("step_number")
    var stepNumber: Int = 0

    @SerializedName("fields")
    lateinit var fields: List<NFormViewProperty>

    constructor()

    constructor(stepName: String) {
        this.stepName = stepName
    }


    constructor(stepName: String, stepNumber: Int, fields: List<NFormViewProperty>) {
        this.stepName = stepName
        this.stepNumber = stepNumber
        this.fields = fields
    }
}
