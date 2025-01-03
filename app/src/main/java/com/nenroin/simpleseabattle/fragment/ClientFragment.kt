package com.nenroin.simpleseabattle.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nenroin.simpleseabattle.activity.ClientActivity
import com.nenroin.simpleseabattle.databinding.FragmentClientBinding

class ClientFragment : Fragment() {
    private lateinit var binding: FragmentClientBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentClientBinding.inflate(inflater, container, false)

        binding.connectButton.setOnClickListener {
            val serverIp = binding.ipEditText.text.toString()
            if (serverIp.isNotEmpty()) {
                (activity as? ClientActivity)?.connectToServer(serverIp)
            } else {
                Toast.makeText(requireContext(), "Enter correct IP address!", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return binding.root
    }
}