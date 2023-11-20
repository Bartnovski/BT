package ru.spectator.bt.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.spectator.bt.databinding.AvailableBtDevicesBinding
import ru.spectator.bt.models.BtModel

class BtAdapter(private val interactionListener: InteractionListener) : ListAdapter<BtModel,BtAdapter.BtHolder>(BTDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BtHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AvailableBtDevicesBinding.inflate(inflater, parent, false)
        return BtHolder(binding,interactionListener)
    }

    override fun onBindViewHolder(holder: BtHolder, position: Int) {
        val btDevice = getItem(position)
        holder.bind(btDevice)
    }

    class BtHolder(
        private val binding : AvailableBtDevicesBinding,
        interactionListener: InteractionListener) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var btDevice: BtModel


        init {
            binding.deviceName.setOnClickListener {
                interactionListener.btClicked(btDevice)
            }
        }

        @SuppressLint("MissingPermission")
        fun bind(btDevice: BtModel) = with(binding) {
            this@BtHolder.btDevice = btDevice
            deviceName.text = btDevice.name ?: "Unnamed"
            deviceMAC.text = btDevice.mac
        }
    }

    private object BTDiffCallback : DiffUtil.ItemCallback<BtModel>() {
        override fun areItemsTheSame(oldItem: BtModel, newItem: BtModel): Boolean {
           return oldItem.mac  == newItem.mac
        }

        override fun areContentsTheSame(oldItem: BtModel, newItem: BtModel): Boolean {
            return oldItem == newItem
        }

    }

}