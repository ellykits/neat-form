package com.nerdstone.neatformcore.domain.builders

import com.nerdstone.neatformcore.domain.view.NFormView

/**
 * Every widget implements this interface. It provides common properties and methods used by individual
 * widgets. For instances [acceptedAttributes] is a set of supported view attributes and  [resourcesMap]
 * is used to store styles that can be applied uniformly to this type of widget
 *
 */
interface ViewBuilder {

    val nFormView: NFormView

    val acceptedAttributes: HashSet<String>

    var resourcesMap: MutableMap<String, Int>

    fun buildView()

    fun setViewProperties(attribute: Map.Entry<String, Any>)

}