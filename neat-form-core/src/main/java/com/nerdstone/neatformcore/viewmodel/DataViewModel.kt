package com.nerdstone.neatformcore.viewmodel

import androidx.lifecycle.ViewModel
import com.nerdstone.neatformcore.domain.model.NFormViewDetails

/**
 * Created by cozej4 on 2019-11-25.
 *
 *@cozej4 https://github.com/cozej4
 */

class DataViewModel : ViewModel() {
    var details: HashMap<String, NFormViewDetails> = HashMap()
}