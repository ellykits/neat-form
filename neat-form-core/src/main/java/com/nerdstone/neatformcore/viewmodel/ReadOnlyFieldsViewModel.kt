package com.nerdstone.neatformcore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author Elly Nerdstone
 *This view model is used to hold state of read only fields. which is a [MutableSet] of field names
 */
class ReadOnlyFieldsViewModel : ViewModel() {
    private val _readOnlyFields: LiveData<MutableSet<String>> =
        MutableLiveData(mutableSetOf())
    val readOnlyFields: LiveData<MutableSet<String>> = _readOnlyFields
}