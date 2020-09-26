package com.nerdstone.neatform.custom.builders

import com.nerdstone.neatform.custom.views.CustomImageView
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.applyViewAttributes
import com.nerdstone.neatformcore.utils.convertEnumToSet
import java.util.*

class CustomImageViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    override val acceptedAttributes = ImageViewProperties::class.java.convertEnumToSet()

    override lateinit var resourcesMap: MutableMap<String, Int>

    private val customImageViewNFormView = nFormView as CustomImageView

    enum class ImageViewProperties {
        SRC
    }

    override fun buildView() {
        imageSetup()
        nFormView.applyViewAttributes(acceptedAttributes, this::setViewProperties)
    }

    private fun imageSetup() {
        customImageViewNFormView.apply {
            maxHeight = 96
            maxWidth = 96
            borderWidth = 1
        }
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        when (attribute.key.toUpperCase(Locale.getDefault())) {
            ImageViewProperties.SRC.name -> {
                val context = customImageViewNFormView.context
                val id = context.resources.getIdentifier(attribute.value.toString(), "drawable", context.packageName)
                customImageViewNFormView.setImageResource(id)
            }
        }
    }
}