package com.nerdstone.neatformcore.form.common

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.nerdstone.neatformcore.R

class FormErrorDialog(context: Context) : AlertDialog(context, R.style.ErrorDialog) {

    private lateinit var dialogView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.form_error_dialog)
        setCanceledOnTouchOutside(false)
        dialogView = window!!.decorView.findViewById(android.R.id.content)
        findViewById<Button>(R.id.error_dialog_button).setOnClickListener {
            this.dismiss()
        }
        val params = window!!.attributes.apply {
            width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
            height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        window!!.attributes = params
    }

}