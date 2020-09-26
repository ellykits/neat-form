package com.nerdstone.neatformcore.utils

/**
 * ======================================================================================================
 * Global Constants
 * ======================================================================================================
 */
const val ID = "id"
const val DRAWABLE = "drawable"
const val VALUE = "value"
const val VALIDATION_RESULT = "validationResults"

object Constants {

    object ThemeColor {
        const val COLOR_ACCENT = "colorAccent"
        const val COLOR_PRIMARY = "colorPrimary"
        const val COLOR_PRIMARY_DARK = "colorPrimaryDark"
    }

    object ViewType {
        const val TOAST_NOTIFICATION = "toast_notification"
        const val NUMBER_SELECTOR = "number_selector"
        const val SPINNER = "spinner"
        const val EDIT_TEXT = "edit_text"
        const val TEXT_INPUT_EDIT_TEXT = "text_input_edit_text"
        const val MULTI_CHOICE_CHECKBOX = "multi_choice_checkbox"
        const val CHECKBOX = "checkbox"
        const val RADIO_GROUP = "radio_group"
        const val DATETIME_PICKER = "datetime_picker"
    }

    object RuleActions {
        const val VISIBILITY = "_visibility"
        const val CALCULATION = "_calculation"
    }

    object NotificationTypes {
        const val INFO = "info"
        const val SUCCESS = "success"
        const val ERROR = "error"
        const val WARNING = "warning"
    }
}
