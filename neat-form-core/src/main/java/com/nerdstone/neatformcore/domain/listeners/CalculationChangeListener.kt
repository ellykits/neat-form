package com.nerdstone.neatformcore.domain.listeners

/**
 * Calculation listener. This listener listens for changes in calculations.
 * Any views implementing this can make use of the updated value
 */
interface CalculationChangeListener {
    /**
     * Callback method called when a [calculationField] has been updated. The callback returns
     * the name of the fields and its corresponding value.
     */
    fun onCalculationChanged(calculationField: Pair<String, Any?>)
}