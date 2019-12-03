package com.nerdstone.neatformcore.domain.model

import java.io.Serializable

data class NFormViewData(
    var value: Any? = null,
    var metadata: Map<String, Any>? = null
) : Serializable