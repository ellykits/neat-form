package com.nerdstone.neatformcore.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nerdstone.neatformcore.domain.model.NFormViewData

/**
 * Created by cozej4 on 2019-11-25.
 *
 *@cozej4 https://github.com/cozej4
 */

class DataViewModel : ViewModel() {

    var details: LiveData<HashMap<String, NFormViewData>> = MutableLiveData(HashMap())

    fun saveFieldValue(fieldName: String, fieldValue: NFormViewData) {
        details.value?.set(fieldName, fieldValue)
    }

    fun updateDetails(newDetails: HashMap<String, NFormViewData>) {
        details.value?.putAll(newDetails)
    }
}