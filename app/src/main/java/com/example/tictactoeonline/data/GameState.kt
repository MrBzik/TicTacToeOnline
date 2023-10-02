package com.example.tictactoeonline.data

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val playerAtTurn : Player? = Player('X', "noname"),
    val field : Array<Array<Char?>> = createEmptyBoard(),
    val winningPlayer : Player? = null,
    val isBoardFull : Boolean = false,
    val connectedPlayers : List<Player> = emptyList()
) {


    companion object{

        fun createEmptyBoard() : Array<Array<Char?>> =
            arrayOf(
                arrayOf(null, null, null),
                arrayOf(null, null, null),
                arrayOf(null, null, null)
            )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameState

        if (playerAtTurn != other.playerAtTurn) return false
        if (!field.contentDeepEquals(other.field)) return false
        if (winningPlayer != other.winningPlayer) return false
        if (isBoardFull != other.isBoardFull) return false
        if (connectedPlayers != other.connectedPlayers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = playerAtTurn?.hashCode() ?: 0
        result = 31 * result + field.contentDeepHashCode()
        result = 31 * result + (winningPlayer?.hashCode() ?: 0)
        result = 31 * result + isBoardFull.hashCode()
        result = 31 * result + connectedPlayers.hashCode()
        return result
    }
}