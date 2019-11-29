package com.nerdstone.neatformcore.domain.model

import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

@Entity
data class NFormViewData(
    @Expose
    var type: String,
    @Expose
    var value: Any? = null,
    @Expose
    var metadata: Map<String, Any>? = null
) : Serializable