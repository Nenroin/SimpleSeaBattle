package com.nenroin.simpleseabattle.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nenroin.simpleseabattle.databinding.FragmentServerBinding
import com.nenroin.simpleseabattle.network.Util.Companion.getLocalIpAddress

class ServerFragment : Fragment() {
    private lateinit var binding: FragmentServerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentServerBinding.inflate(inflater, container, false)

        val ipAddress = getLocalIpAddress()
        binding.ipTextView.text = ipAddress ?: "Not found"

        return binding.root
    }

}