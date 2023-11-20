package ru.spectator.bt.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.spectator.bt.BtViewModel
import ru.spectator.bt.connectivity.BtConnection
import ru.spectator.bt.databinding.MainFragmentBinding


class MainFragment : Fragment() {
    lateinit var btConnection: BtConnection

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = MainFragmentBinding.inflate(inflater, container, false)
        val viewModel: BtViewModel by viewModels(ownerProducer = ::requireParentFragment)



        binding.deviceName.text = viewModel.onBtClickEvent.value!!.name
        binding.deviceMAC.text = viewModel.onBtClickEvent.value!!.mac


//        ReceiveThread.btReceivedData.observe(viewLifecycleOwner) {
//            binding.temperature.text = it
//        }

        return binding.root
    }



}