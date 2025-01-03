package com.nenroin.simpleseabattle.network

import java.io.*
import java.net.Socket

class ClientManager(private val serverIp: String, private val port: Int) : MessageSender {
    private var socket: Socket? = null
    private var listener: ClientListener? = null

    interface ClientListener {
        fun onConnected()
        fun onMessageReceived(message: String)
    }

    fun setListener(listener: ClientListener) {
        this.listener = listener
    }

    fun connectToServer() {
        Thread {
            try {
                socket = Socket(serverIp, port)
                listener?.onConnected()

                val input = socket!!.getInputStream()
                val reader = BufferedReader(InputStreamReader(input))

                while (true) {
                    val message = reader.readLine()
                    if (message != null) {
                        listener?.onMessageReceived(message)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    override fun sendMessage(message: String) {
        Thread {
            try {
                val output = socket?.getOutputStream()
                val writer = PrintWriter(output, true)
                writer.println(message)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    fun disconnect() {
        try {
            socket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
