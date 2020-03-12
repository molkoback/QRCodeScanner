package com.molko.qrcodescanner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_history_item.view.*

class HistoryAdapter(context: Context, private val list: List<String>) : BaseAdapter() {
    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    
    override fun getView(i: Int, view: View?, parent: ViewGroup?): View {
        val row = inflater.inflate(R.layout.list_history_item, parent, false)
        row.itemTime.text = "2020-01-02"
        row.itemText.text = list[i]
        return row
    }
    
    override fun getItem(i: Int): Any {
        return list[i]
    }
    
    override fun getItemId(i: Int): Long {
        return i.toLong()
    }
    
    override fun getCount(): Int {
        return list.size
    }
}
