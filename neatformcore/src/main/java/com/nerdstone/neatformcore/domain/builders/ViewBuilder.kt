package com.nerdstone.neatformcore.domain.builders

interface ViewBuilder {
    val acceptedAttributes: HashSet<String>
    fun buildView()
}