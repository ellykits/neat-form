package com.nerdstone.neatformcore.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author Elly Nerdstone
 *This view model is used to hold state of read only fields. which is a [MutableSet] of field names
 */
class FormViewModel : ViewModel() {

    private val _readOnlyFields = MutableLiveData<MutableSet<String>>(mutableSetOf())

    val readOnlyFields get() = _readOnlyFields

}