package com.nenroin.simpleseabattle.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nenroin.simpleseabattle.fragment.GameFragment
import com.nenroin.simpleseabattle.databinding.ActivityServerBinding
import com.nenroin.simpleseabattle.fragment.ServerFragment
import java.io.*
import java.net.Inet4Address
import java.net.ServerSocket
import java.net.Socket
import java.net.NetworkInterface

class ServerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServerBinding
    private val port = 12345
    private var serverSocket: ServerSocket? = null
    private var clientSocket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, ServerFragment())
                .commit()
        }

        Thread {
            try {
                serverSocket = ServerSocket(port)
                clientSocket = serverSocket!!.accept()

                runOnUiThread {
                    openGameFragment()
                }

                // Обработка постоянного соединения
                val input = clientSocket!!.getInputStream()
                val output = clientSocket!!.getOutputStream()
                val reader = BufferedReader(InputStreamReader(input))
                val writer = PrintWriter(output, true)

                while (true) {
                    val message = reader.readLine()
                    if (message != null) {
                        // Обработка сообщений от клиента
                        runOnUiThread {
                            Toast.makeText(this, "Клиент: $message", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun openGameFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, GameFragment())
            .commit()
    }

    fun getLocalIpAddress(): String? {
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

    override fun onDestroy() {
        super.onDestroy()
        serverSocket?.close()
        clientSocket?.close()
    }
}

