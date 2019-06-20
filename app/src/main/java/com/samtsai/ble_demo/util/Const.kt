package com.samtsai.ble_demo.util

import android.Manifest

class Const {
    companion object {
        val LocationPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}