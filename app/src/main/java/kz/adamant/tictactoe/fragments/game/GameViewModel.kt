package kz.adamant.tictactoe.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kz.adamant.tictactoe.fragments.game.Game

// This is basically needed for surviving configuration changes
class GameViewModel(turn: Array<String>) : ViewModel() {
    val game = Game(turn)
    var gameStateText: String
    var gameState: Int

    init {
        gameStateText = getUserTurnText()
        gameState = Game.GAME_STATE.TIE
    }

    fun getUserTurnText(): String {
        return "Turn: ${getUserWithMark()}"
    }

    fun setUserTurnText() {
        gameStateText = getUserTurnText()
    }

    fun setUserWinsText() {
        gameStateText = "User ${getUserWithMark()} wins!"
        gameState =
            if (game.getUserTurn() == 0) Game.GAME_STATE.WIN_FIRST else Game.GAME_STATE.WIN_SECOND
    }

    fun setTieText() {
        gameStateText = "Tie!"
        gameState = Game.GAME_STATE.TIE
    }

    fun getUserWithMark(): String {
        return "${game.getCurrentTurnUserName()} - ${game.getMarkFromIndex(game.getUserTurn())}"
    }

    var lineType: Game.Line? = null
    var lineX: Int? = null
    var lineY: Int? = null

    var isBottomButtonVisible = false
}

class GameViewModelFactory(private val turn: Array<String>) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = GameViewModel(turn) as T
}