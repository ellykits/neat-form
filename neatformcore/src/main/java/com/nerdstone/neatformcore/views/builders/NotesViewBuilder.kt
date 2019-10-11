package com.nerdstone.neatformcore.views.builders

import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.views.widgets.NotesNFormView

class NotesViewBuilder(override val nFormView: NFormView) : ViewBuilder {
    private val notesNFormView: NotesNFormView = nFormView as NotesNFormView

    override val acceptedAttributes: HashSet<String>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun buildView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}