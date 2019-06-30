package com.samtsai.ble_demo.bluetooth

import android.bluetooth.BluetoothGattCharacteristic
import java.nio.ByteBuffer
import java.nio.ByteOrder

object BleDataExtractor {

    fun getModelNumberString(characteristic: BluetoothGattCharacteristic): String {
        return characteristic.getStringValue(0)
    }

    fun getThingyGravity(characteristic: BluetoothGattCharacteristic): Triple<Float, Float, Float> {
        val raw = characteristic.value
        val byteBuffer = ByteBuffer.wrap(raw)
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
        val gx = byteBuffer.getFloat(0)
        val gy = byteBuffer.getFloat(4)
        val gz = byteBuffer.getFloat(8)

        return Triple(gx, gy, gz)
    }
}