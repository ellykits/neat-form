package com.nerdstone.neatformcore.views.builders

import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.Utils
import com.nerdstone.neatformcore.utils.ViewUtils
import com.nerdstone.neatformcore.utils.getViewsByTagValue
import com.nerdstone.neatformcore.views.widgets.NumberSelectorNFormView
import java.util.*


class NumberSelectorViewBuilder(override val nFormView: NFormView) : ViewBuilder {

    private var visibleNumbers = 1
    private var firstNumber = 1
    private var lastNumber = visibleNumbers
    private var maxValue = visibleNumbers
    private val numberSelectorNFormView = nFormView as NumberSelectorNFormView
    override val acceptedAttributes = Utils.convertEnumToSet(NumberSelectorProperties::class.java)

    enum class NumberSelectorProperties {
        MAX_VALUE, TEXT, FIRST_NUMBER, VISIBLE_NUMBERS
    }

    object Constants {
        const val FIRST_NUMBER = "first_number"
        const val MAX_VALUE = "max_value"
        const val VISIBLE_NUMBERS = "visible_numbers"
        const val ANY_TAG = "any-tag"
    }

    override fun buildView() {
        initNumbers()
        ViewUtils.applyViewAttributes(
            nFormView = numberSelectorNFormView,
            acceptedAttributes = acceptedAttributes,
            task = this::setViewProperties
        )
    }

    private fun initNumbers() {
        numberSelectorNFormView.viewProperties.viewAttributes?.also {
            firstNumber = it[Constants.FIRST_NUMBER].toString().toInt()
            maxValue = it[Constants.MAX_VALUE].toString().toInt()
            visibleNumbers = it[Constants.VISIBLE_NUMBERS].toString().toInt()
        }
    }


    override fun setViewProperties(attribute: Map.Entry<String, Any>) {
        when (attribute.key.toUpperCase(Locale.getDefault())) {
            NumberSelectorProperties.TEXT.name -> {
                numberSelectorNFormView.addView(
                    ViewUtils.addViewLabel(
                        attribute,
                        numberSelectorNFormView
                    )
                )
            }
            NumberSelectorProperties.FIRST_NUMBER.name -> {
                firstNumber = attribute.value.toString().toInt()
            }
            NumberSelectorProperties.MAX_VALUE.name -> {
                maxValue = attribute.value.toString().toInt()
            }
            NumberSelectorProperties.VISIBLE_NUMBERS.name -> {
                createNumberSelectorItems(attribute.value.toString().toInt())
            }
        }
    }

    private fun createNumberSelectorItems(visibleNumbers: Int) {
        this.visibleNumbers = visibleNumbers
        lastNumber = visibleNumbers
        val numberSelectorLayout = LinearLayout(numberSelectorNFormView.context)
        numberSelectorLayout.orientation = LinearLayout.HORIZONTAL
        (firstNumber..this.visibleNumbers).forEach { number ->
            val item = getNumberSelectorItem(number)
            if (this.visibleNumbers == 1) {
                numberSelectorNFormView.addView(item)
                setNumberSelectorBackground(item, false)
                return
            }
            setNumberSelectorBackground(item, false)
            numberSelectorLayout.addView(item)
        }
        numberSelectorNFormView.addView(numberSelectorLayout)
    }

    private fun getNumberSelectorItem(number: Int): TextView {
        val linearLayoutParams =
            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
        linearLayoutParams.setMargins(1, 2, 1, 2)
        var value = "$number"
        if (number == visibleNumbers && maxValue > visibleNumbers) {
            value = "$number +"
        }
        return TextView(numberSelectorNFormView.context)
            .apply {
                layoutParams = linearLayoutParams
                text = value
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                setTag(
                    R.id.number_selector_key,
                    "${numberSelectorNFormView.viewProperties.name}_$number"
                )
                setTag(R.id.is_number_selector, true)
                setPadding(0, 20, 0, 20)
                setOnClickListener {
                    setNumberSelectorBackground(this, true)
                    resetNumberSelectorBackground(getTag(R.id.number_selector_key) as String)
                    if (number == visibleNumbers) {
                        showPopupMenu(this)
                    } else {
                        passValue(this)
                    }
                }
            }
    }

    private fun setNumberSelectorBackground(item: TextView, isSelected: Boolean) {
        when {
            isSelected && lastNumber > 1 -> when (getValue(item.text.toString())) {
                firstNumber -> item.setBackgroundResource(R.drawable.num_selector_selected_left_bg)
                lastNumber -> item.setBackgroundResource(R.drawable.num_selector_selected_right_bg)
                else -> item.setBackgroundResource(R.drawable.num_selector_selected_flat_bg)
            }
            !isSelected && lastNumber > 1 -> when (getValue(item.text.toString())) {
                firstNumber -> item.setBackgroundResource(R.drawable.num_selector_left_bg)
                lastNumber -> item.setBackgroundResource(R.drawable.num_selector_right_bg)
                else -> item.setBackgroundResource(R.drawable.num_selector_flat_bg)
            }
            isSelected && lastNumber == 1 -> item.setBackgroundResource(R.drawable.num_selector_selected_round_bg)
            !isSelected && lastNumber == 1 -> item.setBackgroundResource(R.drawable.num_selector_round_bg)
        }
        setNumberTextColor(item, isSelected)
    }

    private fun setNumberTextColor(item: TextView, isSelected: Boolean) {
        if (isSelected) {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> item.setTextColor(
                    ContextCompat.getColor(numberSelectorNFormView.context, R.color.colorWhite)
                )
                else -> item.setTextColor(
                    numberSelectorNFormView.context.resources
                        .getColor(R.color.colorWhite)
                )
            }
        } else {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> item.setTextColor(
                    ContextCompat.getColor(numberSelectorNFormView.context, R.color.colorBlack)
                )
                else -> item.setTextColor(
                    numberSelectorNFormView.context.resources
                        .getColor(R.color.colorBlack)
                )
            }
        }
    }

    private fun resetNumberSelectorBackground(tag: String) {
        (numberSelectorNFormView as View)
            .getViewsByTagValue(R.id.is_number_selector, true)
            .map { it as TextView }
            .forEach { view ->
                if (view.getTag(R.id.number_selector_key) as String != tag) {
                    setNumberSelectorBackground(view, false)
                }
            }
    }

    private fun getValue(text: String): Int {
        return text.replace("+", "").trim().toInt()
    }

    private fun showPopupMenu(textView: TextView) {
        val popupMenu = PopupMenu(numberSelectorNFormView.context, textView)
        for (i in visibleNumbers + 1..maxValue) {
            popupMenu.menu.add(i.toString())
        }
        popupMenu.setOnMenuItemClickListener { item ->
            lastNumber = item.title.toString().toInt()
            textView.text = item.title
            passValue(textView)
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    private fun passValue(textView: TextView) {
        numberSelectorNFormView.viewDetails.value = textView.text.toString().toInt()
        numberSelectorNFormView.dataActionListener?.onPassData(numberSelectorNFormView.viewDetails)
    }

    fun resetNumberSelectorValue() {
        numberSelectorNFormView.viewDetails.value = null
        numberSelectorNFormView.dataActionListener?.onPassData(numberSelectorNFormView.viewDetails)
        resetNumberSelectorBackground(Constants.ANY_TAG)
    }
}

