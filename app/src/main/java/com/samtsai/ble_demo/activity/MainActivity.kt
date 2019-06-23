package com.samtsai.ble_demo.activity

import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.samtsai.ble_demo.R
import com.samtsai.ble_demo.adapter.ScanListAdapter
import com.samtsai.ble_demo.bluetooth.GattSpec
import com.samtsai.ble_demo.extension.*
import kotlinx.android.synthetic.main.activity_main.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity : AppCompatActivity() {
    companion object {
        const val Request_Enable_Bt = 1
        const val Request_Location_Permission = 2
    }

    lateinit var scanListAdapter: ScanListAdapter

    //TODO: (2) set up BLE - get BluetoothAdapter
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: (1) check if the device supports BLE
        packageManager.takeIf { !it.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }?.also {
            finishWithToast(R.string.msg_not_supported, Toast.LENGTH_LONG)
        }

        scanListAdapter = ScanListAdapter(this)
        listView.apply {
            adapter = scanListAdapter
            onItemClickListener = deviceListCallback
        }
    }

    override fun onResume() {
        super.onResume()

        //TODO: (3) enable BLE, ask location, then scan
        bluetoothAdapter?.run {
            when {
                !isEnabled -> askEnableBT(Request_Enable_Bt)
                !hasLocationPermission -> askLocationPermissions(Request_Location_Permission)
                else -> startBleScan()
            }
        }
    }

    private fun startBleScan() {
        //TODO: (4) start scan device
        bluetoothAdapter?.bluetoothLeScanner?.startScan(leScanCallback)
    }

    override fun onPause() {
        super.onPause()

        stopBleScan()
    }

    private fun stopBleScan() {
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Request_Enable_Bt && resultCode == Activity.RESULT_OK) {
            when (resultCode) {
                Activity.RESULT_OK -> startBleScan()
                else -> showToast(R.string.msg_ble_disabled, Toast.LENGTH_LONG)
            }
        }
    }

    private val leScanCallback = object: ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            //TODO: (5) display scanned device
            result?.device?.run {
                scanListAdapter.apply {
                    add(this@run)
                    notifyDataSetChanged()
                }
            }
        }
    }

    private val deviceListCallback = AdapterView.OnItemClickListener { parent, view, position, id ->
        scanListAdapter.getItem(position)?.run {
            //TODO: (6) connect the device
            stopBleScan()
            connectGatt(this@MainActivity, true, bleConnectCallback)
        }
    }

    private val bleConnectCallback = object: BluetoothGattCallback() {
        //TODO: (7) config the device to send/receive data
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> gatt?.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> initDataConfig(gatt)
            }
        }

        private fun initDataConfig(gatt: BluetoothGatt?) {
            val thingyMotionService = GattSpec.Service.ThingyMotion
            val thingyRawDataCharacteristic = GattSpec.Characteristic.ThingyGravityVector
            gatt?.get(thingyMotionService, thingyRawDataCharacteristic)?.run {
                initCharacteristicReading(gatt, this)
            }
        }

        private fun initCharacteristicReading(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            val properties = characteristic.properties
            if (properties and BluetoothGattCharacteristic.PROPERTY_READ > 0) {
                gatt.readCharacteristic(characteristic)
            }
            if (properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY > 0) {
                gatt.takeIf { it.setCharacteristicNotification(characteristic, true) }.run {
                    characteristic.descriptors.forEach {
                        it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(it)
                    }
                }
            }
        }

        //TODO: (8) handle data
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            onHandleData(characteristic)
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int ) {
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> onHandleData(characteristic)
            }
        }

        private fun onHandleData(characteristic: BluetoothGattCharacteristic?) {
            when (characteristic?.uuid) {
                GattSpec.Characteristic.ThingyGravityVector.uuid -> {
                    val raw = characteristic.value
                    val byteBuffer = ByteBuffer.wrap(raw)
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
                    val gx = byteBuffer.getFloat(0)
                    val gy = byteBuffer.getFloat(4)
                    val gz = byteBuffer.getFloat(8)

                    runOnUiThread {
                        val text = "(%3.2f, %3.2f, %3.2f)".format(gx, gy, gz)
                        textView.text = text
                    }
                }
            }
        }
    }

}
