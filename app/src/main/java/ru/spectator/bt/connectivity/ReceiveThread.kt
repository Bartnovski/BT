package ru.spectator.bt.connectivity

import android.bluetooth.BluetoothSocket
import ru.spectator.bt.adapter.InteractionListener
import ru.spectator.bt.models.BtModel
import ru.spectator.bt.utils.SingleLiveEvent
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import kotlin.properties.Delegates

class ReceiveThread(btSocket: BluetoothSocket) : Thread() {

    private var inStream: InputStream? = null
    private var outStream: OutputStream? = null

    private var size by Delegates.notNull<Int>()

    init {
        try {
            inStream = btSocket.inputStream

        } catch (i: IOException) {

        }

        try {
            outStream = btSocket.outputStream

        } catch (i: IOException) {

        }
    }

    override fun run() {
        val buff = ByteArray(512)

        while (true) {
            try {
                size = inStream?.read(buff)!!
                btReceivedData.value = String(buff,0, size)
            } catch (i: IOException) {
                break
            }
        }
    }

    fun sendMessage(byteArray: ByteArray) {
        try {
            outStream?.write(byteArray)
        } catch (i: IOException) {

        }
    }

    companion object {
        var btReceivedData = SingleLiveEvent<String>()
    }
}