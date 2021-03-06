package kz.adamant.tictactoe.fragments.game

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import kz.adamant.tictactoe.*
import kz.adamant.tictactoe.databinding.FragmentGameBinding
import kz.adamant.tictactoe.game.GameViewModel
import kz.adamant.tictactoe.game.GameViewModelFactory
import kz.adamant.tictactoe.util.BindingFragment
import kz.adamant.tictactoe.util.DrawLine


class GameFragment : BindingFragment<FragmentGameBinding>(FragmentGameBinding::inflate) {

    private val navArgs by navArgs<GameFragmentArgs>()

    // ViewModel carries about configuration changes, all objects inside will be kept
    private val gameVM by viewModels<GameViewModel> {
        GameViewModelFactory(
            arrayOf(
                navArgs.user1,
                navArgs.user2
            )
        )
    }

    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        game = gameVM.game
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateTopText()
        // Post method needed to get width and height after layout was constructed
        // Otherwise it gives 0 for both sides
        binding.tictactoeLayout.post {
            generateTable(binding.tictactoeLayout)
        }

        binding.btnContinue.setOnClickListener {
            navController.navigate(
                R.id.action_gameFragment_to_userRecordsFragment,
            )
        }
    }

    private fun updateTopText() {
        binding.tvUserTurn.text = gameVM.gameStateText
    }

    private fun generateTable(layout: RelativeLayout) {
        val table = game.table
        val width = layout.width / 3.0
        val height = layout.height / 3.0

        // Set Bottom button visibility when configuration changes happen
        if (gameVM.isBottomButtonVisible)
            binding.btnContinue.visibility = View.VISIBLE


//        Log.d("TAG", "generateTable: ${table.contentDeepToString()}")
        for (i in table.indices) {
            for (j in table[i].indices) {
                val item = layoutInflater.inflate(R.layout.tictactoe_item, layout, false)
                val image = item.findViewById<ImageView>(R.id.image)

                // Set Images if configuration changes happen
                if (table[i][j] != -1) setImage(image, table[i][j])

                item.setOnClickListener {
                    val move = game.makeMove(i, j)
                    when (move) {
                        is Game.Move.Regular -> {
                            setImage(image, game.getUserTurn())
                            game.updateCurrentTurn()
                            gameVM.setUserTurnText()
                            updateTopText()
                        }
                        is Game.Move.Winning -> {
                            setImage(image, game.getUserTurn())
                            makeButtonVisible()
                            setDrawLine(move.line, layout, i, j, width, height)
                            gameVM.setUserWinsText()
                            updateTopText()
                            gameVM.updateUserRecords(navArgs.user1, navArgs.user2, gameVM.gameState)
                        }
                        is Game.Move.Tie -> {
                            setImage(image, game.getUserTurn())
                            makeButtonVisible()
                            gameVM.setTieText()
                            updateTopText()
                            gameVM.updateUserRecords(navArgs.user1, navArgs.user2, gameVM.gameState)
                        }
                        is Game.Move.CannotMove -> return@setOnClickListener
                    }
                }
                // This layout params needed to construct tictactoe table by giving margins
                // The alternative way is to use GridLayout. whatever
                val layoutParams = RelativeLayout.LayoutParams(width.toInt(), height.toInt())
                layoutParams.topMargin = (height * i).toInt()
                layoutParams.leftMargin = (width * j).toInt()
                item.layoutParams = layoutParams

                layout.addView(item)
            }
        }
        // Set Draw Line if win when conf changes happen
        if (gameVM.lineType != null && gameVM.lineX != null && gameVM.lineY != null)
            setDrawLine(
                gameVM.lineType!!,
                layout,
                gameVM.lineX!!,
                gameVM.lineY!!,
                width,
                height
            )
    }

    private fun setImage(image: ImageView, value: Int) {
        if (value == 0) {
            image.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_x
                )
            )
        } else if (value == 1) {
            image.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_o
                )
            )
        }
    }

    private fun makeButtonVisible() {
        if (binding.btnContinue.visibility != View.VISIBLE) {
            binding.btnContinue.visibility = View.VISIBLE
            gameVM.isBottomButtonVisible = true
        }
    }

    private fun setDrawLine(
        line: Game.Line,
        layout: RelativeLayout,
        i: Int,
        j: Int,
        width: Double,
        height: Double
    ) {
        gameVM.lineType = line
        gameVM.lineX = i
        gameVM.lineY = j
        when (line) {
            Game.Line.COL -> drawColLine(
                layout,
                j,
                width,
                height
            )
            Game.Line.ROW -> drawRowLine(
                layout,
                i,
                width,
                height
            )
            Game.Line.L_DIAG -> drawLDiagLine(
                layout,
                width,
                height
            )
            Game.Line.R_DIAG -> drawRDiagLine(
                layout,
                width,
                height
            )
        }
    }

    private fun drawRDiagLine(
        layout: RelativeLayout,
        width: Double,
        height: Double
    ) {
        val startX = width * 2.75
        val startY = height / 4.0
        val endX = width / 4.0
        val endY = height * 2.75
        val line = DrawLine(
            requireContext(),
            startX.toFloat(),
            startY.toFloat(),
            endX.toFloat(),
            endY.toFloat()
        )
        layout.addView(line)
    }

    private fun drawLDiagLine(
        layout: RelativeLayout,
        width: Double,
        height: Double
    ) {
        val startX = width / 4.0
        val startY = height / 4.0
        val endX = width * 2.75
        val endY = height * 2.75
        val line = DrawLine(
            requireContext(),
            startX.toFloat(),
            startY.toFloat(),
            endX.toFloat(),
            endY.toFloat()
        )
        layout.addView(line)
    }

    private fun drawRowLine(
        layout: RelativeLayout,
        i: Int,
        width: Double,
        height: Double
    ) {
        val startX = width / 4.0
        val startY = (i * height) + height / 2.0
        val endX = width * 2.75
        val endY = startY
        val line = DrawLine(
            requireContext(),
            startX.toFloat(),
            startY.toFloat(),
            endX.toFloat(),
            endY.toFloat()
        )
        layout.addView(line)
    }

    private fun drawColLine(
        layout: RelativeLayout,
        j: Int,
        width: Double,
        height: Double
    ) {
        val startX = (j * width) + width / 2.0
        val startY = height / 4.0
        val endX = startX
        val endY = height * 2.75
        val line = DrawLine(
            requireContext(),
            startX.toFloat(),
            startY.toFloat(),
            endX.toFloat(),
            endY.toFloat()
        )
        layout.addView(line)
    }
}