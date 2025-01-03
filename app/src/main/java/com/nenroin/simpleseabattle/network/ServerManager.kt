package com.nenroin.simpleseabattle.network

import java.io.*
import java.net.ServerSocket
import java.net.Socket

class ServerManager(private val port: Int) : MessageSender {
    private var serverSocket: ServerSocket? = null
    private var clientSocket: Socket? = null
    private var listener: ServerListener? = null

    interface ServerListener {
        fun onClientConnected()
        fun onMessageReceived(message: String)
    }

    fun setListener(listener: ServerListener) {
        this.listener = listener
    }

    fun startServer() {
        Thread {
            try {
                serverSocket = ServerSocket(port)
                clientSocket = serverSocket!!.accept()

                listener?.onClientConnected()

                val input = clientSocket!!.getInputStream()
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
                val output = clientSocket?.getOutputStream()
                val writer = PrintWriter(output, true)
                writer.println(message)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    fun stopServer() {
        try {
            serverSocket?.close()
            clientSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
