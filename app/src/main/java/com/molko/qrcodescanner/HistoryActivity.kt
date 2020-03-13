package com.molko.qrcodescanner

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    lateinit var dialogClear: AlertDialog
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        
        // Clear history dialog
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.history_clear))
            .setPositiveButton(getString(android.R.string.ok)) { _, _ -> historyClear() }
            .setNegativeButton(getString(android.R.string.cancel)) { _, _ -> }
        dialogClear = builder.create()
        
        // History list
        val items = listOf(
            "QR data",
            "More QR data",
            "One more QR data"
        )
        val adapter = HistoryAdapter(applicationContext, items)
        listHistory.adapter = adapter
        listHistory.setOnItemClickListener { _, _, i, _ ->
            val text = adapter.getItem(i) as String
            showQRDialog(this, text)
        }
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.itemHistoryClear -> {
                dialogClear.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    fun historyClear() {
        // TODO
    }
}
