package com.molko.qrcodescanner

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.qrdialog.view.*

private fun copy(text: String) {
    // TODO
}

private fun open(text: String) {
    // TODO
}

fun showQRDialog(context: Context, text: String) {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.qrdialog, null)
    view.textQR.text = text
    view.buttonCopy.setOnClickListener {
        copy(text)
    }
    view.buttonOpen.setOnClickListener {
        open(text)
    }
    
    val builder = AlertDialog.Builder(context)
    builder.setView(view)
    builder.create().show()
}
