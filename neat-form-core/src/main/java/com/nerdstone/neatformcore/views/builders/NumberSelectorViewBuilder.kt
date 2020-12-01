package com.nerdstone.neatformcore.views.builders

import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.nerdstone.neatformcore.R
import com.nerdstone.neatformcore.domain.builders.ViewBuilder
import com.nerdstone.neatformcore.domain.view.NFormView
import com.nerdstone.neatformcore.utils.*
import com.nerdstone.neatformcore.views.widgets.NumberSelectorNFormView
import timber.log.Timber
import java.util.*

open class NumberSelectorViewBuilder(final override val nFormView: NFormView) : ViewBuilder {

    private var firstNumber = 1
    private var visibleNumbers = 1
    private var lastNumber = visibleNumbers
    private var maxValue = visibleNumbers
    private val numberSelectorNFormView = nFormView as NumberSelectorNFormView
    override val acceptedAttributes = NumberSelectorProperties::class.java.convertEnumToSet()
    override lateinit var resourcesMap: MutableMap<String, Int>

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
        createLabel()
        initNumbers()
        numberSelectorNFormView.applyViewAttributes(acceptedAttributes, this::setViewProperties)
    }

    private fun createLabel() {
        val text = numberSelectorNFormView.viewProperties.viewAttributes?.get(
            NumberSelectorProperties.TEXT.name.toLowerCase(
                Locale.getDefault()
            )
        )
        numberSelectorNFormView.addView(
            numberSelectorNFormView.addViewLabel(Pair(NumberSelectorProperties.TEXT.name, text!!))
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
                setNumberSelectorBackground(item, selected = false)
                numberSelectorLayout.addView(item)
                numberSelectorNFormView.addView(numberSelectorLayout)
                return
            }
            setNumberSelectorBackground(item, selected = false)
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
                    setNumberSelectorBackground(this, selected = true)
                    resetNumberSelectorBackground(getTag(R.id.number_selector_key) as String)
                    if (number == visibleNumbers && maxValue > visibleNumbers) {
                        showPopupMenu(this)
                    } else {
                        passValue(this)
                    }
                }
            }
    }

    private fun setNumberSelectorBackground(
        item: TextView, selected: Boolean, enabled: Boolean = true
    ) {
        when {
            selected && lastNumber > 1 -> when (getValue(item.text.toString())) {
                firstNumber -> {
                    item.setBackgroundResource(R.drawable.num_selector_selected_left_bg)
                    changeDrawableColor(item, enabled)
                }
                lastNumber -> {
                    item.setBackgroundResource(R.drawable.num_selector_selected_right_bg)
                    changeDrawableColor(item, enabled)
                }
                else -> {
                    item.setBackgroundResource(R.drawable.num_selector_selected_flat_bg)
                    changeDrawableColor(item, enabled)
                }
            }
            !selected && lastNumber > 1 -> when (getValue(item.text.toString())) {
                firstNumber -> item.setBackgroundResource(R.drawable.num_selector_left_bg)
                lastNumber -> item.setBackgroundResource(R.drawable.num_selector_right_bg)
                else -> item.setBackgroundResource(R.drawable.num_selector_flat_bg)
            }
            selected && lastNumber == 1 -> {
                item.setBackgroundResource(R.drawable.num_selector_selected_round_bg)
                changeDrawableColor(item, enabled)
            }
            !selected && lastNumber == 1 -> item.setBackgroundResource(R.drawable.num_selector_round_bg)
        }
        setNumberTextColor(item, selected)
    }

    private fun changeDrawableColor(item: TextView, enabled: Boolean) {
        with(item.background as GradientDrawable) {
            when (enabled) {
                true -> {
                    apply {
                        colors = intArrayOf(
                            R.color.numberSelectorItemSelected, R.color.numberSelectorItemSelected
                        )
                    }
                }
                else -> {
                    apply {
                        colors = intArrayOf(R.color.colorDarkGrey, R.color.colorDarkGrey)
                    }
                }
            }
        }
    }

    private fun setNumberTextColor(item: TextView, isSelected: Boolean) {
        if (isSelected) {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> item.setTextColor(
                    ContextCompat.getColor(numberSelectorNFormView.context, R.color.colorWhite)
                )
                else -> item.setTextColor(
                    ContextCompat.getColor(item.context, R.color.colorWhite)
                )
            }
        } else {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> item.setTextColor(
                    ContextCompat.getColor(numberSelectorNFormView.context, R.color.colorBlack)
                )
                else -> item.setTextColor(
                    ContextCompat.getColor(item.context, R.color.colorBlack)
                )
            }
        }
    }

    private fun resetNumberSelectorBackground(tag: String) {
        getNumberSelectorTextViews()
            .forEach { view ->
                if (view.getTag(R.id.number_selector_key) as String != tag) {
                    setNumberSelectorBackground(view, selected = false)
                }
            }
    }

    private fun getNumberSelectorTextViews(): List<TextView> {
        return (numberSelectorNFormView as View)
            .getViewsByTagValue(R.id.is_number_selector, true)
            .map { it as TextView }
    }

    private fun getValue(text: String): Int {
        return text.replace("+", "").trim().toInt()
    }

    private fun showPopupMenu(textView: TextView) {
        val popupMenu = PopupMenu(numberSelectorNFormView.context, textView)
        for (i in visibleNumbers..maxValue) {
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

    fun setValue(value: Any, enabled: Boolean) {
        val textViews = getNumberSelectorTextViews()
        val index = when (value) {
            is Double -> value.toInt()
            is Int -> value
            else -> -1
        }
        if (index <= maxValue) {
            var textView: TextView? = null
            if (index in 0..visibleNumbers) {
                textView = textViews[index]
            } else if (index >= visibleNumbers) {
                lastNumber = index
                textView = textViews[visibleNumbers].apply {
                    text = index.toString()
                }
            }
            textView?.also {
                setNumberSelectorBackground(it, selected = true, enabled = enabled)
                resetNumberSelectorBackground(it.getTag(R.id.number_selector_key) as String)
                passValue(it)
            }
            textViews.forEach { it.setReadOnlyState(enabled) }
        } else {
            Toast.makeText(
                numberSelectorNFormView.context,
                "Error setting value for ${numberSelectorNFormView.viewDetails.name} -> index $index out of bounds.",
                Toast.LENGTH_SHORT
            ).show()
            Timber.e("Error setting number selector value index ($index) > than max ($maxValue)")
        }
    }
}

