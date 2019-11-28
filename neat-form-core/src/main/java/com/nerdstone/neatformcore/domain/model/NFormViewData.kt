package com.nerdstone.neatformcore.domain.model

import androidx.room.Entity

import java.io.Serializable

@Entity
data class NFormViewData(
    var value: Any? = null,
    var metadata: Map<String, Any>? = null
) : Serializable