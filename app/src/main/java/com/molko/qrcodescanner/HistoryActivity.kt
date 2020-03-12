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
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    historyClear()
                }
                DialogInterface.BUTTON_NEGATIVE -> {}
            }
        }
        val builder = AlertDialog.Builder(this)
        builder
            .setMessage(getString(R.string.history_clear))
            .setPositiveButton(getString(android.R.string.ok), listener)
            .setNegativeButton(getString(android.R.string.cancel), listener)
        dialogClear = builder.create()
        
        // Dummy list
        val items = listOf(
            "QR data",
            "More QR data",
            "One more QR data"
        )
        listHistory.adapter = HistoryAdapter(applicationContext, items)
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
