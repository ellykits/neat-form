package com.nerdstone.neatformcore.views.builders

import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.widgets.SpinnerNFormView
import timber.log.Timber
import java.util.*

class SpinnerViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private val spinnerNFormView = nFormView as SpinnerNFormView
    private val spinnerOptions = mutableListOf<String>()
    private val materialSpinner = SmartMaterialSpinner<String>(spinnerNFormView.context)

    enum class SpinnerProperties {
        TEXT,SEARCHABLE
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
                SpinnerProperties.TEXT.name -> {
                    materialSpinner.hint = attribute.value as String
                    formatHintForRequiredFields()
                }
                SpinnerProperties.SEARCHABLE.name -> {
                    isSearchable=true
                    searchHeaderText=attribute.value as String
                    setSearchHeaderBackgroundColor(Utils.getThemeAccentColors(spinnerNFormView.context))
                }

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

            item = spinnerOptions
            hintColor = R.color.colorBlack

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (position > 0) {
                        spinnerNFormView.viewDetails.value = item
                    } else {
                        spinnerNFormView.viewDetails.value = null
                    }
                    spinnerNFormView.dataActionListener?.onPassData(spinnerNFormView.viewDetails)
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {
                    errorText = "Nothing Selected"
                }
            }

        }

        spinnerNFormView.addView(materialSpinner)
    }

    fun resetSpinnerValue() {
        materialSpinner.setSelection(0)
        spinnerNFormView.viewDetails.value = null
        spinnerNFormView.dataActionListener?.onPassData(spinnerNFormView.viewDetails)
    }

    private fun formatHintForRequiredFields() {
        if (spinnerNFormView.viewProperties.requiredStatus != null) {
            if (Utils.isFieldRequired(spinnerNFormView)) {
                materialSpinner.hint =
                    ViewUtils.addRedAsteriskSuffix(materialSpinner.hint.toString())
            }
        }
    }
}