package com.nerdstone.neatform.form

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nerdstone.neatform.R

class FormRecyclerAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<FormRecyclerAdapter.MainViewHolder>() {

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
            holder.formCategoryTextView?.text = it.formCategory.capitalize()
            holder.formTitleTextView?.text = it.formTitle.capitalizeWords()
        }
    }

    inner class MainViewHolder(rowSection: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(rowSection) {

        val formTitleTextView = rowSection.findViewById<TextView?>(R.id.formTitleTextView)
        val formCategoryTextView = rowSection.findViewById<TextView?>(R.id.formCategoryTextView)

        init {
            rowSection.tag = this
            rowSection.setOnClickListener {
                listener.onClick(it)
            }
        }

    }
}


fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }