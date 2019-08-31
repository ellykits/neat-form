package com.nerdstone.neatformcore.domain.model

import android.arch.persistence.room.Entity

import java.io.Serializable

@Entity
class NFormViewData : Serializable {
    var name: String? = null
    var value: Any? = null
    var label: String? = null
    var metadata: Map<String, Any>? = null
}
