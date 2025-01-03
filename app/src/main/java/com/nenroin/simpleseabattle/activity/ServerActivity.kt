package com.nenroin.simpleseabattle.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nenroin.simpleseabattle.fragment.GameFragment
import com.nenroin.simpleseabattle.databinding.ActivityServerBinding
import com.nenroin.simpleseabattle.fragment.ServerFragment
import com.nenroin.simpleseabattle.logic.GameManager
import com.nenroin.simpleseabattle.network.ServerManager

class ServerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServerBinding
    private lateinit var serverManager: ServerManager
    private lateinit var gameManager: GameManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        serverManager = ServerManager(12345)
        gameManager = GameManager(serverManager, true)

        serverManager.setListener(object : ServerManager.ServerListener {
            override fun onClientConnected() {
                runOnUiThread {
                    Toast.makeText(this@ServerActivity, "Клиент подключен", Toast.LENGTH_SHORT)
                        .show()
                    openGameFragment()
                }
            }

            override fun onMessageReceived(message: String) {
                runOnUiThread {
                    Toast.makeText(this@ServerActivity, "Клиент: $message", Toast.LENGTH_SHORT)
                        .show()
                    gameManager.getMessage(message)
                }
            }
        })

        serverManager.startServer()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, ServerFragment())
                .commit()
        }
    }

    private fun openGameFragment() {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, GameFragment(gameManager))
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        serverManager.stopServer()
    }
}

