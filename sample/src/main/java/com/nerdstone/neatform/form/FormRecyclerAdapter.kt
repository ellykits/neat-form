package com.nerdstone.neatform.form

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nerdstone.neatform.R
import java.util.Locale

class FormRecyclerAdapter : RecyclerView.Adapter<FormRecyclerAdapter.MainViewHolder>() {

    var formList = mutableListOf<FormData>()
    lateinit var listener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val rowSection = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_row_item,
            parent, false
        )
        return MainViewHolder(rowSection)
    }

    override fun getItemCount(): Int {
        return formList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val form: FormData = formList[position]
        form.also {
            holder.formCategoryTextView.text = it.formCategory.capitalize(Locale.ROOT)
            holder.formTitleTextView.text = it.formTitle.capitalizeWords()
        }
    }

    inner class MainViewHolder(rowSection: View) : RecyclerView.ViewHolder(rowSection) {

        val formTitleTextView: TextView = rowSection.findViewById(R.id.formTitleTextView)
        val formCategoryTextView: TextView = rowSection.findViewById(R.id.formCategoryTextView)

        init {
            rowSection.tag = this
            rowSection.setOnClickListener {
                listener.onClick(it)
            }
        }

    }
}


fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize(Locale.ROOT) }