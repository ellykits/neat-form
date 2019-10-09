package com.nerdstone.neatformcore.views.builders

import android.app.DatePickerDialog
import android.support.design.widget.TextInputEditText
import android.widget.DatePicker
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.views.widgets.DateTimePickerNFormView
import java.text.SimpleDateFormat
import java.util.*


class DateTimePickerViewBuilder(override val nFormView: NFormView) :
    DatePickerDialog.OnDateSetListener, ViewBuilder {

    private var dateDisplayFormat = "YYYY-MM-dd"
    private val dateTimePickerNFormView = nFormView as DateTimePickerNFormView
    private val textInputEditText = TextInputEditText(dateTimePickerNFormView.context)
    override val acceptedAttributes = Utils.convertEnumToSet(DateTimePickerProperties::class.java)
    private val selectedDate = Calendar.getInstance()

    enum class DateTimePickerProperties {
        HINT, TYPE, DISPLAY_FORMAT
    }

    private object DatePickerType {
        const val DATE_PICKER = "date_picker"
        const val TIME_PICKER = "time_picker"
    }

    override fun buildView() {
        ViewUtils.applyViewAttributes(
            nFormView = dateTimePickerNFormView,
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )

        dateTimePickerNFormView.addView(textInputEditText)
        textInputEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
            0, 0, R.drawable.ic_today, 0
        )
        textInputEditText.compoundDrawablePadding = 8
        textInputEditText.isFocusable = false
    }

    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        textInputEditText.apply {
            when (attribute.key.toUpperCase(Locale.getDefault())) {
                DateTimePickerProperties.HINT.name -> {
                    hint = attribute.value.toString()
                }
                DateTimePickerProperties.TYPE.name -> {
                    when {
                        attribute.value.toString() == DatePickerType.DATE_PICKER -> {
                            textInputEditText.setOnClickListener {
                                launchDatePickerDialog()
                            }
                        }
                        attribute.value.toString() == DatePickerType.TIME_PICKER -> {
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

    private fun launchDatePickerDialog() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(dateTimePickerNFormView.context, this, year, month, dayOfMonth)
        datePickerDialog.show()

    }

    private fun launchTimePickerDialog() {

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        selectedDate.set(year, month, dayOfMonth)
        textInputEditText.setText(getFormattedDate())
        dateTimePickerNFormView.viewDetails.value = selectedDate.timeInMillis
        dateTimePickerNFormView.dataActionListener?.onPassData(dateTimePickerNFormView.viewDetails)
    }

    private fun getFormattedDate(): String =
        SimpleDateFormat(
            dateDisplayFormat,
            Locale.getDefault()
        ).format(Date(selectedDate.timeInMillis))

}