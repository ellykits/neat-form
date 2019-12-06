package com.nerdstone.neatform.utils

import android.app.AlertDialog
import android.content.Context

object DialogUtil {
    fun createAlertDialog(context: Context, title: String, message: String): AlertDialog.Builder {
      return AlertDialog.Builder(context)
        .apply {
            setTitle(title)
            setMessage(message)
            create()
        }
    }
}