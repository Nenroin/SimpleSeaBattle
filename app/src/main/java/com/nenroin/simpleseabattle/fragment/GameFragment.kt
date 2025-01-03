package com.nenroin.simpleseabattle.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.nenroin.simpleseabattle.R
import com.nenroin.simpleseabattle.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private lateinit var playerCells: Array<Array<Button>>
    private lateinit var enemyCells: Array<Array<Button>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)

        playerCells = Array(10) { Array(10) { Button(requireContext()) } }
        enemyCells = Array(10) { Array(10) { Button(requireContext()) } }

        createBoard(binding.gridLayoutYourBoard, playerCells, false)
        createBoard(binding.gridLayoutEnemyBoard, enemyCells, true)

        return binding.root
    }

    private fun createBoard(
        gridLayout: GridLayout,
        cells: Array<Array<Button>>,
        isEnemyBoard: Boolean
    ) {
        for (i in 0 until 10) {
            for (j in 0 until 10) {
                val cell = Button(requireContext())
                cell.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(i, 1f)
                    columnSpec = GridLayout.spec(j, 1f)
                }

                if ((i + j) % 2 == 0) {
                    cell.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.board_variant_1
                        )
                    )
                } else {
                    cell.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.board_variant_2
                        )
                    )
                }

                if (isEnemyBoard) {
                    cell.setOnClickListener {
                        onEnemyCellClick(i, j)
                    }
                } else {
                    cell.isEnabled = false
                }

                cells[i][j] = cell
                gridLayout.addView(cell)
            }
        }

        gridLayout.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val width = gridLayout.width
                gridLayout.layoutParams.height = width
                gridLayout.visibility = View.VISIBLE
                gridLayout.requestLayout()
                gridLayout.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
    }

    private fun onEnemyCellClick(row: Int, col: Int) {
        enemyCells[row][col].setBackgroundColor(Color.RED)
    }
}
