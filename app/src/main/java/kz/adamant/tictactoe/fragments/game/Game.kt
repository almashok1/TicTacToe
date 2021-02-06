package kz.adamant.tictactoe.fragments.game

import kotlin.random.Random

class Game(
    /**
     * Usernames and their indices
     *
     * 0 index -> X
     *
     * 1 index -> O
     */
    private val turn: Array<String>
) {

    companion object GAME_STATE {
        const val WIN_FIRST = 1
        const val WIN_SECOND = 2
        const val TIE = 3
    }

    private var _currentTurn: Int

    init {
        _currentTurn = Random.nextInt(2)
    }

    private var _moveCount = 0
    private var gameOver = false

    fun getMarkFromIndex(index: Int): Char {
        if (index == 0) return 'X'
        else if (index == 1) return 'O'
        return ' '
    }

    private fun incrementMoveCount() {
        _moveCount++
    }

    /**
     * Table of TicTacToe
     *
     * -1 -> EmptyCell
     *
     * 0 -> X turn
     *
     * 1 -> O turn
     */
    private val _table = arrayOf(
        intArrayOf(-1, -1, -1),
        intArrayOf(-1, -1, -1),
        intArrayOf(-1, -1, -1)
    )
    val table get() = _table

    fun updateCurrentTurn() {
        _currentTurn = 1 - _currentTurn
    }

    fun getUserTurn() = _currentTurn

    fun getCurrentTurnUserName() = turn[getUserTurn()]


    sealed class Move {
        object Regular : Move()
        class Winning(val line: Line) : Move()
        object Tie : Move()
        object CannotMove : Move()
    }

    fun makeMove(i: Int, j: Int): Move {
        if (table[i][j] == -1 && !gameOver) {
            incrementMoveCount()
            // Update table state
            table[i][j] = getUserTurn()
            // Checking
            val winningLine = checkForWinning(i, j)

            return if (winningLine == null && _moveCount == 9)
                Move.Tie.also { gameOver = true }
            else if (winningLine != null)
                Move.Winning(winningLine).also { gameOver = true }
            else
                Move.Regular
        }

        return Move.CannotMove
    }

    /**
     * Returns integer displaying winning state
     * @param x x position of player
     * @param y y position of player
     * @return Game.Line enum,
     * if the game is still continuing returns null
     */
    fun checkForWinning(x: Int, y: Int): Line? {
        val player = getUserTurn()
        var col = 0
        var row = 0
        var ldiag = 0
        var rdiag = 0
        val n = table.size - 1
        for (i in 0..n) {
            if (table[x][i] == player) row++
            if (table[i][y] == player) col++
            if (table[i][i] == player) ldiag++
            if (table[i][n - i] == player) rdiag++
        }
        if (row == n + 1 || col == n + 1 || ldiag == n + 1 || rdiag == n + 1) {
            if (row == n + 1) return Line.ROW
            if (col == n + 1) return Line.COL
            if (ldiag == n + 1) return Line.L_DIAG
            if (rdiag == n + 1) return Line.R_DIAG
        }
        return null
    }

    enum class Line {
        COL, ROW, L_DIAG, R_DIAG
    }
}