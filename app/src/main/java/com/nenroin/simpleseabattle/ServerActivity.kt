package com.nenroin.simpleseabattle

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nenroin.simpleseabattle.databinding.ActivityServerBinding
import java.io.*
import java.net.Inet4Address
import java.net.ServerSocket
import java.net.Socket
import java.net.NetworkInterface

class ServerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServerBinding
    private val port = 12345

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ipAddress = getLocalIpAddress()

        binding.ipTextView.text = ipAddress ?: "Не найден"

        Thread {
            try {
                val serverSocket = ServerSocket(port)
                val clientSocket: Socket = serverSocket.accept()

                val inputStream = clientSocket.getInputStream()
                val reader = BufferedReader(InputStreamReader(inputStream))
                val message = reader.readLine()

                runOnUiThread {
                    Toast.makeText(this, "Received: $message", Toast.LENGTH_SHORT).show()
                }

                clientSocket.close()
                serverSocket.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun getLocalIpAddress(): String? {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            for (networkInterface in interfaces) {
                val addresses = networkInterface.inetAddresses
                for (inetAddress in addresses) {
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
