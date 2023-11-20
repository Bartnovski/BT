package ru.spectator.bt.adapter

import androidx.lifecycle.LiveData
import ru.spectator.bt.models.BtModel

interface Repository {

   val btDevices : LiveData<List<BtModel>>

   fun btConnect(btModel: BtModel)
}