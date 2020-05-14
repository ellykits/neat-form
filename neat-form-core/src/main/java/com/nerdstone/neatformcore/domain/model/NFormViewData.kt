package com.nerdstone.neatformcore.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NFormViewData(
    @Expose
    var type: String? = null,
    @Expose
    var value: Any? = null,
    @Expose
    @SerializedName("meta_data", alternate = ["metadata"])
    var metadata: Map<String, Any>? = null
) : Serializable