package ru.spectator.bt.adapter

import android.bluetooth.BluetoothDevice
import ru.spectator.bt.models.BtModel
import androidx.lifecycle.LiveData
interface InteractionListener {

    fun btClicked(btModel: BtModel)


}