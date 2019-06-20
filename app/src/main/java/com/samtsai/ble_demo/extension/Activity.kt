package com.samtsai.ble_demo.extension

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.core.app.ActivityCompat
import com.samtsai.ble_demo.util.Const

fun Activity.askEnableBT(requestCode: Int) {
    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
    startActivityForResult(enableBtIntent, requestCode)
}

fun Activity.askLocationPermissions(requestCode: Int) {
    ActivityCompat.requestPermissions(this, Const.LocationPermissions, requestCode)
}

fun Activity.finishWithToast(res: Int, duration: Int) {
    showToast(res, duration)
    finish()
}

fun Activity.finishWithToast(text: String, duration: Int) {
    showToast(text, duration)
    finish()
}