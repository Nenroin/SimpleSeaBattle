package com.nenroin.simpleseabattle.logic

import kotlin.random.Random

class Board(private val size: Int) {
    private val body: MutableList<MutableList<ECellType>> =
        MutableList(size) { MutableList(size) { ECellType.EMPTY } }

    private var lastX: Int = -1
    private var lastY: Int = -1
    private var lastCellType: ECellType = ECellType.EMPTY

    fun getLastX() = lastX
    fun getLastY() = lastY
    fun getLastCellType() = lastCellType
    fun getBoard() = body.toList()

    fun setBoardCell(x: Int, y: Int, cellType: ECellType){
        if (x !in body.indices || y !in body[x].indices) {
            throw IllegalArgumentException("The coordinates ($x, $y) are outside the field.")
        }

        body[x][y] = cellType
    }

    fun isAllShipsKilled(): Boolean {
        return body.flatten().none { it == ECellType.SHIP }
    }

    fun hit(x: Int, y: Int): ECellType {
        if (x !in body.indices || y !in body[x].indices) {
            throw IllegalArgumentException("The coordinates ($x, $y) are outside the field.")
        }

        val currentCell = body[x][y]

        if (currentCell == ECellType.HITTED_SHIP || currentCell == ECellType.HITTED_EMPTY) {
            return currentCell
        }

        body[x][y] = when (currentCell) {
            ECellType.SHIP -> ECellType.HITTED_SHIP
            else -> ECellType.HITTED_EMPTY
        }

        return currentCell
    }

    fun setShips() {
        val ships = listOf(
            4 to 1,
            3 to 2,
            2 to 3,
            1 to 4
        )

        for ((shipSize, count) in ships) {
            repeat(count) {
                placeShip(shipSize)
            }
        }
    }

    private fun placeShip(shipSize: Int) {
        while (true) {
            val isHorizontal = Random.nextBoolean()
            val row = Random.nextInt(size)
            val col = Random.nextInt(size)

            if (canPlaceShip(row, col, shipSize, isHorizontal)) {
                placeShipAt(row, col, shipSize, isHorizontal)
                break
            }
        }
    }

    private fun canPlaceShip(row: Int, col: Int, shipSize: Int, isHorizontal: Boolean): Boolean {
        if (isHorizontal) {
            if (col + shipSize > size) return false
            for (i in 0 until shipSize) {
                if (!isCellAvailable(row, col + i)) return false
            }
        } else {
            if (row + shipSize > size) return false
            for (i in 0 until shipSize) {
                if (!isCellAvailable(row + i, col)) return false
            }
        }
        return true
    }

    private fun isCellAvailable(row: Int, col: Int): Boolean {
        if (body[row][col] != ECellType.EMPTY) return false
        for (r in maxOf(0, row - 1)..minOf(size - 1, row + 1)) {
            for (c in maxOf(0, col - 1)..minOf(size - 1, col + 1)) {
                if (body[r][c] == ECellType.SHIP) return false
            }
        }
        return true
    }

    private fun placeShipAt(row: Int, col: Int, shipSize: Int, isHorizontal: Boolean) {
        if (isHorizontal) {
            for (i in 0 until shipSize) {
                body[row][col + i] = ECellType.SHIP
            }
        } else {
            for (i in 0 until shipSize) {
                body[row + i][col] = ECellType.SHIP
            }
        }
    }

    internal fun printBoard() {
        for (row in body) {
            println(row.joinToString(" ") { if (it == ECellType.SHIP) "0" else "." })
        }
    }
}

fun main() {
    val board = Board(10)
    board.setShips()
    board.printBoard()
}