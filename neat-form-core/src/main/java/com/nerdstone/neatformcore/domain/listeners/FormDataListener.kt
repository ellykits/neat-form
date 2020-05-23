package com.nerdstone.neatformcore.domain.listeners

import com.nerdstone.neatformcore.domain.model.NFormViewData

/**
 * @author Elly Nerdstone
 *
 * This interface is used by the form builder to communicate with the steps.
 * [onReceiveDataEvent] method is called when the values of the fields in the form are updated.
 *
 * This updated record is passed to the view model that the Steps(Fragments) are watching on.
 */

interface FormDataListener {
    /**
     * When new form [formFieldsData] has been received by the form builder, it is passed to this method.
     */
    fun onReceiveDataEvent(formFieldsData: FormFieldsData)
}

class FormFieldsData(val data: HashMap<String, NFormViewData>)