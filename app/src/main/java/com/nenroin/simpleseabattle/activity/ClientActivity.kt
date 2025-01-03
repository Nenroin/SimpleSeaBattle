package com.nenroin.simpleseabattle.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nenroin.simpleseabattle.fragment.GameFragment
import com.nenroin.simpleseabattle.databinding.ActivityClientBinding
import com.nenroin.simpleseabattle.fragment.ClientFragment
import com.nenroin.simpleseabattle.logic.GameManager
import com.nenroin.simpleseabattle.network.ClientManager

class ClientActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientBinding
    private lateinit var clientManager: ClientManager
    private lateinit var gameManager: GameManager

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
        clientManager = ClientManager(serverIp, 12345)
        gameManager = GameManager(clientManager, false)

        clientManager.setListener(object : ClientManager.ClientListener {
            override fun onConnected() {
                runOnUiThread {
                    Toast.makeText(this@ClientActivity, "Подключено к серверу", Toast.LENGTH_SHORT).show()
                    openGameFragment()
                }
            }

            override fun onMessageReceived(message: String) {
                runOnUiThread {
                    Toast.makeText(this@ClientActivity, "Сервер: $message", Toast.LENGTH_SHORT).show()
                    gameManager.getMessage(message)
                }
            }
        })

        clientManager.connectToServer()
    }

    private fun openGameFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, GameFragment(gameManager))
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        clientManager.disconnect()
    }
}
