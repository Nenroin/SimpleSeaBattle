package com.nenroin.simpleseabattle.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nenroin.simpleseabattle.fragment.GameFragment
import com.nenroin.simpleseabattle.databinding.ActivityClientBinding
import com.nenroin.simpleseabattle.fragment.ClientFragment
import java.io.*
import java.net.Socket

class ClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientBinding
    private val port = 12345
    private var socket: Socket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, ClientFragment())
                .commit()
        }
    }

    fun connectToServer(serverIp: String) {
        Thread {
            try {
                socket = Socket(serverIp, port)

                runOnUiThread {
                    openGameFragment()
                }

                val input = socket!!.getInputStream()
                val output = socket!!.getOutputStream()
                val reader = BufferedReader(InputStreamReader(input))
                val writer = PrintWriter(output, true)

                // Пример отправки сообщения на сервер
                writer.println("Клиент подключился!")

                while (true) {
                    val message = reader.readLine()
                    if (message != null) {
                        runOnUiThread {
                            Toast.makeText(this, "Сервер: $message", Toast.LENGTH_SHORT).show()
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

    override fun onDestroy() {
        super.onDestroy()
        socket?.close()
    }
}
