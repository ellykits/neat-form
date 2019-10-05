package com.nerdstone.neatformcore.domain.builders

import com.nerdstone.neatformcore.domain.view.NFormView

interface ViewBuilder {

    val nFormView: NFormView

    val acceptedAttributes: HashSet<String>

    fun buildView()

    fun setViewProperties(attribute: Map.Entry<String, Any>)

}