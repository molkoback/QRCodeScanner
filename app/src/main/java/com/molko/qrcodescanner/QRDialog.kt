package com.molko.qrcodescanner

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface.OnDismissListener
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.qrdialog.view.*

private fun copy(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(context.getString(R.string.app_name), text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, context.getString(R.string.copy_done), Toast.LENGTH_SHORT).show()
}

private fun open(context: Context, text: String) {
    // TODO
}

fun showQRDialog(context: Context, text: String, listener: OnDismissListener? = null) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.qrdialog, null)
    view.textQR.text = text
    view.buttonCopy.setOnClickListener {
        copy(context, text)
    }
    view.buttonOpen.setOnClickListener {
        open(context, text)
    }
    
    val builder = AlertDialog.Builder(context)
    if (listener != null) {
        builder.setOnDismissListener(listener)
    }
    builder.setView(view)
    builder.create().show()
}
