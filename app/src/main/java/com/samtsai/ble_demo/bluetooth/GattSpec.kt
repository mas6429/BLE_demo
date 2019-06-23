package com.samtsai.ble_demo.bluetooth

import java.util.*

class GattSpec {

    enum class Service(val uuid: UUID) {
        ThingyMotion(UUID.fromString("ef680400-9b35-4933-9b10-52ffa9740042"))
    }

    enum class Characteristic(val uuid: UUID) {
        ThingyGravityVector(UUID.fromString("ef68040a-9b35-4933-9b10-52ffa9740042"))
    }

}