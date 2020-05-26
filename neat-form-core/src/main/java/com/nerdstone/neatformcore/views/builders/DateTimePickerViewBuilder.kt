package com.nerdstone.neatformcore.views.builders

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.widgets.DateTimePickerNFormView
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

open class DateTimePickerViewBuilder(final override val nFormView: NFormView) :
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, ViewBuilder {

    private var dateDisplayFormat = "yyyy-MM-dd"
    private val dateTimePickerNFormView = nFormView as DateTimePickerNFormView
    override val acceptedAttributes = Utils.convertEnumToSet(DateTimePickerProperties::class.java)
    private val calendar = getInstance()
    val selectedDate: Calendar = getInstance()
    val textInputEditText =
        com.google.android.material.textfield.TextInputEditText(
            dateTimePickerNFormView.context
        )

    enum class DateTimePickerProperties {
        HINT, TYPE, DISPLAY_FORMAT
    }

    private object DatePickerType {
        const val DATE_PICKER = "date_picker"
        const val TIME_PICKER = "time_picker"
    }

    override fun buildView() {
        ViewUtils.applyViewAttributes(
            dateTimePickerNFormView, acceptedAttributes, this::setViewProperties
        )

        dateTimePickerNFormView.addView(textInputEditText)
        textInputEditText.compoundDrawablePadding = 8
        textInputEditText.isFocusable = false
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        textInputEditText.apply {
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                DateTimePickerProperties.HINT.name -> {
                    hint = attribute.value.toString()
                    formatHintForRequiredFields()
                }
                DateTimePickerProperties.TYPE.name -> {
                    when {
                        attribute.value.toString() == DatePickerType.DATE_PICKER -> {
                            textInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0, 0, R.drawable.ic_today, 0
                            )
                            textInputEditText.setOnClickListener {
                                launchDatePickerDialog()
                            }
                        }
                        attribute.value.toString() == DatePickerType.TIME_PICKER -> {
                            textInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0, 0, R.drawable.ic_schedule, 0
                            )
                            textInputEditText.setOnClickListener {
                                launchTimePickerDialog()
                            }
                        }
                    }
                }
                DateTimePickerProperties.DISPLAY_FORMAT.name -> {
                    dateDisplayFormat = attribute.value.toString()
                }
            }
        }
    }

    private fun formatHintForRequiredFields() {
        if (dateTimePickerNFormView.viewProperties.requiredStatus != null) {
            if (Utils.isFieldRequired(dateTimePickerNFormView)) {
                textInputEditText.hint =
                    ViewUtils.addRedAsteriskSuffix(textInputEditText.hint.toString())
            }
        }
    }

    private fun launchDatePickerDialog() {
        val year = selectedDate.get(YEAR)
        val month = selectedDate.get(MONTH)
        val dayOfMonth = selectedDate.get(DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(dateTimePickerNFormView.context, this, year, month, dayOfMonth)
        datePickerDialog.show()

    }

    private fun launchTimePickerDialog() {
        val hour = selectedDate.get(HOUR_OF_DAY)
        val minute = selectedDate.get(MINUTE)
        val isTwentyFourHour = false
        val timePickerDialog =
            TimePickerDialog(dateTimePickerNFormView.context, this, hour, minute, isTwentyFourHour)
        timePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedDate.set(year, month, dayOfMonth)
        updateViewData()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        selectedDate.set(
            calendar.get(YEAR), calendar.get(MONTH), calendar.get(DAY_OF_MONTH), hourOfDay, minute
        )
        updateViewData()
    }

    fun updateViewData() {
        textInputEditText.setText(getFormattedDate())
        dateTimePickerNFormView.viewDetails.value = selectedDate.timeInMillis
        dateTimePickerNFormView.dataActionListener?.onPassData(dateTimePickerNFormView.viewDetails)
    }

    private fun getFormattedDate(): String = SimpleDateFormat(dateDisplayFormat, Locale.getDefault())
        .format(Date(selectedDate.timeInMillis))

    fun resetDatetimePickerValue() {
        textInputEditText.setText("")
        dateTimePickerNFormView.viewDetails.value = null
        dateTimePickerNFormView.dataActionListener?.onPassData(dateTimePickerNFormView.viewDetails)
    }
}