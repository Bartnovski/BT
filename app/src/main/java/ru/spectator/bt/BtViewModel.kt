package ru.spectator.bt

import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.*
import android.content.Context
import android.icu.util.ULocale.getName
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.spectator.bt.adapter.InteractionListener
import ru.spectator.bt.models.BtModel
import ru.spectator.bt.utils.SingleLiveEvent
import java.util.Locale


@SuppressLint("MissingPermission")
class BtViewModel(application: Application) : AndroidViewModel(application), InteractionListener {

    private val _devices: MutableLiveData<List<BtModel>> = MutableLiveData()
    val devices: LiveData<List<BtModel>> get() = _devices

    val btManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val pairedDevices: Set<BluetoothDevice> = btManager.adapter.bondedDevices
    private var scanner: BluetoothLeScanner? = null
    private var callback: BleScanCallback? = null
    var gatt: BluetoothGatt? = null
    val gattCallback = GattCallback()
    val tempList = ArrayList<BtModel>()

    private val settings: ScanSettings
    private val filters: List<ScanFilter>

    val foundDevices = HashMap<String, BluetoothDevice>()


    val onBtClickEvent = SingleLiveEvent<BtModel>()


    init {

        settings = buildScanSettings()
        filters = buildFilter()

        pairedDevices.forEach {
            tempList.add(BtModel(it.name,it.address))
        }
    }

    private fun buildScanSettings() =
        ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
            .build()

    private fun buildFilter() =
        listOf(
            ScanFilter.Builder()
                .build()
        )

    fun startScan() {

            callback = BleScanCallback()
            scanner = btManager.adapter.bluetoothLeScanner
            scanner?.startScan(callback)
    }

    fun stopScan() {
        if(callback != null) {
            scanner?.stopScan(callback)
            scanner = null
            callback = null
            Log.d("Scan","Stopped")
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopScan()
    }

    override fun btClicked(btModel: BtModel) {
        onBtClickEvent.value = btModel
    }

    inner class BleScanCallback : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            foundDevices[result.device.address] = result.device

            _devices.postValue(foundDevices.values.map {
                BtModel(it.name,it.address)
            }.toList())

            Log.d("Scanned","Found a device")
        }


        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            results.forEach { result ->
                foundDevices[result.device.address] = result.device
            }
            _devices.postValue(foundDevices.values.map {
                BtModel(it.name,it.address)
            }.toList())
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BluetoothScanner", "onScanFailed:  scan error $errorCode")
        }
    }

    inner class GattCallback : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if(status == GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    // Мы подключились, можно запускать обнаружение сервисов
                    gatt?.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    // Мы успешно отключились (контролируемое отключение)
                    gatt?.close();
                } else {
                    // мы или подключаемся или отключаемся, просто игнорируем эти статусы
                }
            } else {
                // Произошла ошибка... разбираемся, что случилось!

                gatt?.close();
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {

            val services: List<BluetoothGattService> = gatt?.services!!
            }
        }
    }
