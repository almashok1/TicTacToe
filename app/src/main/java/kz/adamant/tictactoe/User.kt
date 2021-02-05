package kz.adamant.tictactoe

data class User(
    val name: String,
    var wins: Int = 0,
    var loses: Int = 0,
    var ties: Int = 0
)