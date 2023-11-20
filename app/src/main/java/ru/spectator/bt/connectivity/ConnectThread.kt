package ru.spectator.bt.connectivity

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import ru.spectator.bt.ui.BTFeed
import java.io.IOException
import java.util.*

@SuppressLint("MissingPermission")
class ConnectThread(private val device: BluetoothDevice) : Thread() {
    //private val uuid = "00001101-0000-1000-8000-00805F9B34FB"
    private val uuid = "0000FFE0-10000-1000-8000-00805F9B34FB"
    private var socket: BluetoothSocket? = null
    lateinit var receiveThread: ReceiveThread

    init {
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        } catch (e: IOException) {

        }
    }

    @SuppressLint("MissingPermission")
    override fun run() {
       try {
           socket?.connect()
           Log.d("BtLog","Connected")
           receiveThread = ReceiveThread(socket!!)
       } catch (e: IOException){
           Log.d("BtLog","Can not connect to device")
           closeConnection()
       }
    }

    private fun closeConnection() {
        try {
            socket?.close()
        } catch (e: IOException){

        }
    }
}