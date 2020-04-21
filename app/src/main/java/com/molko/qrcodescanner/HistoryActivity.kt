package com.molko.qrcodescanner

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_history.*
import org.jetbrains.anko.*

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
        refreshList()
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

    private fun refreshList() {
        doAsync {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, TABLE_NAME).build()
            val entries = db.historyDao().getEntries()
            db.close()
            
            uiThread {
                val adapter = HistoryAdapter(this@HistoryActivity, entries)
                listHistory.adapter = adapter
                listHistory.setOnItemClickListener { _, _, i, _ ->
                    val entry = adapter.getItem(i) as QREntry
                    showQRDialog(this@HistoryActivity, entry.text)
                }
            }
        }
    }
    
    private fun historyClear() {
        doAsync {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, TABLE_NAME).build()
            db.historyDao().clear()
            db.close()
            refreshList()
            uiThread {
                toast(getString(R.string.history_clear_done))
            }
        }
    }
}
