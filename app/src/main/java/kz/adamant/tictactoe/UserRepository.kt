package kz.adamant.tictactoe

import kz.adamant.tictactoe.fragments.game.Game

object UserRepository {
    private val _userRecords by lazy {
        // Took arrayList for that purpose
        // We can use hash map to take the user record, but sometimes it may take O(N), thanks for amortized analysis
        // Later on, to convert it into list of user records it also takes O(n+s), so bad idea!
        // Set does not include searching and returning value
        mutableListOf<User>() // Old fashion way )
    }

    val userRecords get() = _userRecords

    fun addUser(name: String, gameState: Int, userTurn: Int) {
        val user = User(name = name)
        updateUser(user, gameState, userTurn)
        userRecords.add(user)
    }

    enum class SearchSuccess {
        FOUND_ALL, FOUND_FIRST, FOUND_SECOND, FOUND_NONE
    }

    fun searchUsers(name1: String, name2: String, gameState: Int): SearchSuccess {
        var success = SearchSuccess.FOUND_NONE
        userRecords.forEach {
            if (it.name == name1) {
                updateUser(it, gameState, 1)
                success =
                    if (success == SearchSuccess.FOUND_SECOND)
                        SearchSuccess.FOUND_ALL
                    else SearchSuccess.FOUND_FIRST
            } else if (it.name == name2) {
                updateUser(it, gameState, 2)
                success =
                    if (success == SearchSuccess.FOUND_FIRST)
                        SearchSuccess.FOUND_ALL
                    else SearchSuccess.FOUND_SECOND
            }
        }
        return success
    }

    private fun updateUser(user: User, gameState: Int, userTurn: Int) {
        when (gameState) {
            // when user wins
            userTurn -> {
                user.wins++
            }
            // when tie
            Game.GAME_STATE.TIE -> {
                user.ties++
            }
            // when user loses
            else -> user.loses++
        }
    }
}