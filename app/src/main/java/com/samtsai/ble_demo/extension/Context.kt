package com.samtsai.ble_demo.extension

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.samtsai.ble_demo.util.Const

val Context.hasLocationPermission: Boolean
    get() = Const.LocationPermissions.any {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

fun Context.showToast(res: Int, duration: Int) {
    Toast.makeText(this, res, duration).show()
}

fun Context.showToast(text: String, duration: Int) {
    Toast.makeText(this, text, duration).show()
}