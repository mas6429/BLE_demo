package com.samtsai.ble_demo.bluetooth

import java.util.*

class GattSpec {

    enum class Service(val uuid: UUID) {
        // Standard.
        DeviceInformation(UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb")),
        // Nordic.
        ThingyMotion(UUID.fromString("ef680400-9b35-4933-9b10-52ffa9740042"))
    }

    enum class Characteristic(val uuid: UUID) {
        // Standard.
        ModelNumberString(UUID.fromString("00002a24-0000-1000-8000-00805f9b34fb")),
        FirmwareRevisionString(UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb")),
        // Nordic.
        ThingyGravityVector(UUID.fromString("ef68040a-9b35-4933-9b10-52ffa9740042"))
    }

}