package ru.spectator.bt.connectivity

import android.bluetooth.BluetoothAdapter

class BtConnection(val adapter: BluetoothAdapter) {
    private lateinit var connectThread: ConnectThread

    fun connect(mac: String) {
        if (adapter.isEnabled && mac.isNotEmpty()) {
            val device = adapter.getRemoteDevice(mac)
            device.let {
                connectThread = ConnectThread(it)
                connectThread.start()
            }
        }
    }

    fun sendMessage(message: String) {
        connectThread.receiveThread.sendMessage(message.toByteArray())
    }
}