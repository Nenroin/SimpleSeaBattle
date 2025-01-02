package com.nenroin.simpleseabattle

import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nenroin.simpleseabattle.databinding.ActivityClientBinding
import java.io.*
import java.net.Socket

class ClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientBinding
    private val port = 12345

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ipEditText: EditText = binding.ipEditText
        val connectButton: Button = binding.connectButton

        connectButton.setOnClickListener {
            val serverIp = ipEditText.text.toString()
            if (serverIp.isNotEmpty()) {
                connectToServer(serverIp)
            } else {
                Toast.makeText(this, "Please enter a valid IP address", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun connectToServer(serverIp: String) {
        Thread {
            try {
                val socket = Socket(serverIp, port)

                val outputStream = socket.getOutputStream()
                val writer = BufferedWriter(OutputStreamWriter(outputStream))
                writer.write("Hello, Server!")
                writer.newLine()
                writer.flush()

                socket.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }
}