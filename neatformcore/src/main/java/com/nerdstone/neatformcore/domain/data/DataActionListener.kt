package com.nerdstone.neatformcore.domain.data

import com.nerdstone.neatformcore.domain.model.NFormViewDetails

interface DataActionListener {

    fun onPassData(viewDetails: NFormViewDetails)

}
