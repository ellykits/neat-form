package com.nerdstone.neatformcore.viewmodel

import androidx.lifecycle.ViewModel
import com.nerdstone.neatformcore.domain.model.NFormViewData

/**
 * Created by cozej4 on 2019-11-25.
 *
 *@cozej4 https://github.com/cozej4
 */

class DataViewModel : ViewModel() {
    var saveData: HashMap<String, NFormViewData> = HashMap()
}