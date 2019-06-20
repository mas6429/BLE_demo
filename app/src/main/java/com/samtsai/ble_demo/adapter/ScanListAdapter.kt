package com.samtsai.ble_demo.adapter

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ScanListAdapter(context: Context): ArrayAdapter<BluetoothDevice>(context, android.R.layout.simple_list_item_1) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        getItem(position)?.run {
            val text = "$address - $name"
            val textView = view.findViewById<TextView>(android.R.id.text1)
            textView.text = text
        }

        return view
    }

    override fun add(device: BluetoothDevice?) {
        if (getPosition(device) < 0) {
            super.add(device)
        }
    }

}