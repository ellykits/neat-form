package com.nerdstone.neatformcore.domain.view

import com.nerdstone.neatformcore.domain.model.NFormViewDetails

interface DataActionListener {

    fun onPassData(viewDetails: NFormViewDetails)
}
