package com.nerdstone.neatformcore.views.builders

import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.model.NFormViewData
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.*
import com.nerdstone.neatformcore.views.widgets.SpinnerNFormView
import java.util.*

open class SpinnerViewBuilder(final override val nFormView: NFormView) : ViewBuilder {

    private val spinnerNFormView = nFormView as SpinnerNFormView
    private val spinnerOptions = mutableListOf<String>()
    private val optionsNamesMap = hashMapOf<Int, String>()
    val materialSpinner = SmartMaterialSpinner<String>(spinnerNFormView.context)

    enum class SpinnerProperties {
        TEXT, SEARCHABLE
    }

    override val acceptedAttributes get() = SpinnerProperties::class.java.convertEnumToSet()

    override lateinit var resourcesMap: MutableMap<String, Int>

    override fun buildView() {
        spinnerNFormView.applyViewAttributes(acceptedAttributes, this::setViewProperties)
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
                    isSearchable = true
                    searchHeaderText = attribute.value as String
                    setSearchHeaderBackgroundColor(
                        spinnerNFormView.context.getThemeColor(Constants.ThemeColor.COLOR_PRIMARY)
                    )
                }
            }
        }
    }

    private fun addSpinnerOptions() {
        val options = spinnerNFormView.viewProperties.options
        options?.also {
            options.forEachIndexed { index, subViewProperty ->
                spinnerOptions.add(subViewProperty.text)
                optionsNamesMap[index] = subViewProperty.name
            }
        }

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = 0

        materialSpinner.apply {
            layoutParams = params
            item = spinnerOptions
            selectedItemListColor = this.context.getThemeColor(Constants.ThemeColor.COLOR_ACCENT)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    adapterView: AdapterView<*>, view: View, position: Int, id: Long
                ) {
                    with(spinnerNFormView) {
                        val optionText = optionsNamesMap.getValue(position)
                        viewDetails.value =
                            NFormViewData(null, item[position], getOptionMetadata(optionText))
                        dataActionListener?.onPassData(viewDetails)
                    }
                }

                override fun onNothingSelected(adapterView: AdapterView<*>) {
                    with(spinnerNFormView) {
                        viewDetails.value = null
                        dataActionListener?.onPassData(viewDetails)
                    }
                }
            }
        }
        spinnerNFormView.addView(materialSpinner)
    }

    fun resetSpinnerValue() {
        materialSpinner.clearSelection()
        with(spinnerNFormView) {
            viewDetails.value = null
            dataActionListener?.onPassData(viewDetails)
        }
    }

    private fun formatHintForRequiredFields() {
        with(spinnerNFormView) {
            if (!viewProperties.requiredStatus.isNullOrBlank() && isFieldRequired()) {
                materialSpinner.hint = materialSpinner.hint.toString().addRedAsteriskSuffix()
            }
        }
    }
}