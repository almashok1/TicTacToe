package kz.adamant.tictactoe.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kz.adamant.tictactoe.UserRepository
import kz.adamant.tictactoe.fragments.game.Game

// This is basically needed for surviving configuration changes
class GameViewModel(turn: Array<String>) : ViewModel() {

    // Global Scope singleton repository, it will be destroyed as application destroys
    private val userRepository = UserRepository

    val game = Game(turn)
    var gameStateText: String
    var gameState: Int

    init {
        gameStateText = getUserTurnText()
        gameState = Game.GAME_STATE.TIE
    }

    var lineType: Game.Line? = null
    var lineX: Int? = null
    var lineY: Int? = null

    var isBottomButtonVisible = false


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

    fun updateUserRecords(user1Name: String, user2Name: String, gameState: Int) {
        val searchSuccess =
            userRepository.searchUsers(user1Name, user2Name, gameState)

        when (searchSuccess) {
            UserRepository.SearchSuccess.FOUND_ALL -> return
            UserRepository.SearchSuccess.FOUND_FIRST ->
                userRepository.addUser(user2Name, gameState, 2)
            UserRepository.SearchSuccess.FOUND_SECOND ->
                userRepository.addUser(user1Name, gameState, 1)
            UserRepository.SearchSuccess.FOUND_NONE -> {
                userRepository.addUser(user1Name, gameState, 1)
                userRepository.addUser(user2Name, gameState, 2)
            }
        }
    }
}

class GameViewModelFactory(private val turn: Array<String>) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = GameViewModel(turn) as T
}