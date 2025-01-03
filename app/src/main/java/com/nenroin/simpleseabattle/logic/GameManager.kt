package com.nenroin.simpleseabattle.logic

import android.graphics.Color
import android.widget.Button
import com.nenroin.simpleseabattle.network.MessageSender

class GameManager(private val messageSender: MessageSender, isServer: Boolean) {
    private lateinit var playerCells: Array<Array<Button>>
    private lateinit var enemyCells: Array<Array<Button>>

    private var isCanIMove: Boolean = isServer
    private val myBoard: Board = Board(10)

    fun startGame(playerCells: Array<Array<Button>>, enemyCells: Array<Array<Button>>) {
        this.playerCells = playerCells
        this.enemyCells = enemyCells

        myBoard.setShips()

        for (i in this.playerCells.indices) {
            for (j in this.playerCells.indices) {
                if (myBoard.getBoard()[i][j] == ECellType.SHIP) {
                    this.playerCells[i][j].setBackgroundColor(Color.WHITE)
                }
            }
        }
    }

    fun hitEnemy(x: Int, y: Int) {
        if (isCanIMove) {
            messageSender.sendMessage("Hit: $x $y")
            isCanIMove = false
        }
    }

    private fun setMyBoardValue(x: Int, y: Int, cellType: ECellType) {
        if (cellType == ECellType.SHIP) {
            playerCells[x][y].setBackgroundColor(Color.RED)
        } else {
            playerCells[x][y].setBackgroundColor(Color.GREEN)
        }
    }

    private fun setEnemyBoardValue(x: Int, y: Int, cellType: ECellType) {
        if (cellType == ECellType.SHIP) {
            enemyCells[x][y].setBackgroundColor(Color.RED)
        } else {
            enemyCells[x][y].setBackgroundColor(Color.GREEN)
        }
    }

    fun getMessage(message: String) {
        if (message[0] == 'H') {
            val regex = "\\d+".toRegex()

            val matchResult = regex.findAll(message)
            val numbers = matchResult.map { it.value.toInt() }.toList()

            val x: Int = numbers[0]
            val y: Int = numbers[1]

            val cellType: ECellType = myBoard.hit(x, y)

            if (cellType == ECellType.HITTED_SHIP || cellType == ECellType.HITTED_EMPTY) {
                messageSender.sendMessage("E")
            } else if (myBoard.isAllShipsKilled()) {
                messageSender.sendMessage("W")
            } else {
                setMyBoardValue(x, y, cellType)
                messageSender.sendMessage("D: $x $y $cellType")

                if (cellType == ECellType.EMPTY) {
                    isCanIMove = true
                }
            }
        } else if (message[0] == 'D') {
            val regex = """D:\s(\d+)\s(\d+)\s(\w+)""".toRegex()
            val matchResult = regex.find(message)

            if (matchResult != null) {
                val x = matchResult.groupValues[1].toInt()
                val y = matchResult.groupValues[2].toInt()
                val cellType = ECellType.valueOf(matchResult.groupValues[3])

                setEnemyBoardValue(x, y, cellType)

                if (cellType == ECellType.SHIP) {
                    isCanIMove = true
                }
            }
        } else if (message[0] == 'W') {

        } else if (message[0] == 'E') {
            isCanIMove = true
        }
    }
}