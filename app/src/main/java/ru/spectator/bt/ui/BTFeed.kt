package ru.spectator.bt.ui

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import ru.spectator.bt.BtViewModel
import ru.spectator.bt.R
import ru.spectator.bt.adapter.BtAdapter
import ru.spectator.bt.connectivity.BtConnection
import ru.spectator.bt.databinding.BtFeedBinding
import ru.spectator.bt.models.BtModel

class BTFeed : Fragment() {

    private lateinit var binding: BtFeedBinding
    private lateinit var btConnection: BtConnection
    private lateinit var deviceForGatt: BluetoothDevice

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BtFeedBinding.inflate(inflater, container, false)
        val viewModel: BtViewModel by viewModels(ownerProducer = ::requireParentFragment)

        val foundDevicesAdapter = BtAdapter(viewModel)
        binding.availableDevicesRecycler.adapter = foundDevicesAdapter

        viewModel.startScan()

        foundDevicesAdapter.submitList(viewModel.tempList)

        viewModel.devices.observe(viewLifecycleOwner) {
            foundDevicesAdapter.submitList(it)
        }

        viewModel.onBtClickEvent.observe(viewLifecycleOwner) { btModel->
            viewModel.stopScan()
            viewModel.foundDevices.values.map { device ->
                if(device.address == btModel.mac) deviceForGatt = device
            }
            viewModel.gatt = deviceForGatt.connectGatt(context,false,viewModel.gattCallback,BluetoothDevice.TRANSPORT_LE)

//            btConnection = BtConnection(viewModel.btManager.adapter)
//            btConnection.connect(btModel.mac)
            findNavController().navigate(R.id.action_BTFeed_to_mainFragment)
        }
        return binding.root
    }
}

