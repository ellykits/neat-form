package com.nerdstone.neatformcore.views.builders

import android.widget.LinearLayout
import com.jaredrummler.materialspinner.MaterialSpinner
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.widgets.SpinnerNFormView
import java.util.*

class SpinnerViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private val spinnerNFormView = nFormView as SpinnerNFormView
    private val spinnerOptions = mutableListOf<String>()
    private val materialSpinner = MaterialSpinner(spinnerNFormView.context)

    enum class SpinnerProperties {
        TEXT
    }

    override val acceptedAttributes get() = Utils.convertEnumToSet(SpinnerProperties::class.java)

    override fun buildView() {
        ViewUtils.applyViewAttributes(
            nFormView = spinnerNFormView,
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )
        addSpinnerOptions()
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        materialSpinner.apply {
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                SpinnerProperties.TEXT.name -> spinnerNFormView.addView(
                    ViewUtils.addViewLabel(
                        attribute,
                        spinnerNFormView
                    )
                )
            }
        }
    }

    private fun addSpinnerOptions() {
        val options = spinnerNFormView.viewProperties.options
        options?.also {
            options.forEach { spinnerOptions.add(it.text) }
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        materialSpinner.apply {
            layoutParams = params
            setItems(spinnerOptions)
            setBackgroundResource(R.drawable.spinner_bg)
            setHintColor(R.color.colorBlack)
            setOnItemSelectedListener { _, pos, _, item ->
                if (pos > 0) {
                    spinnerNFormView.viewDetails.value = item
                } else {
                    spinnerNFormView.viewDetails.value = null
                }
                spinnerNFormView.dataActionListener?.onPassData(spinnerNFormView.viewDetails)
            }

        }

        spinnerNFormView.addView(materialSpinner)
    }

    fun resetSpinnerValue() {
        materialSpinner.selectedIndex = 0
        spinnerNFormView.viewDetails.value = null
        spinnerNFormView.dataActionListener?.onPassData(spinnerNFormView.viewDetails)
    }
}