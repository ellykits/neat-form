package com.nerdstone.neatform.form

import java.io.Serializable

data class FormData(
    var formTitle: String,
    var formCategory: String,
    val filePath: String
) : Serializable
