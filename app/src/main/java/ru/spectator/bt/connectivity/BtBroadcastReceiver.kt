package ru.spectator.bt.connectivity

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BtBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        when(p1?.action) {
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                Log.d("availableDevices","Action found")

            }
        }
    }
}