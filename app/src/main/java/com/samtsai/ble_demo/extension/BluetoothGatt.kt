package com.samtsai.ble_demo.extension

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.samtsai.ble_demo.bluetooth.GattSpec.*

fun BluetoothGatt.get(service: Service, characteristic: Characteristic): BluetoothGattCharacteristic? =
    try {
        getService(service.uuid).getCharacteristic(characteristic.uuid)
    }
    catch (_: Exception) {
        null
    }