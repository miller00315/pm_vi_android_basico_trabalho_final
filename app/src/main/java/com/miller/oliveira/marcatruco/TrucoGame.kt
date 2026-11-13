package com.miller.oliveira.marcatruco

class Player(
    var name: String,
    var points: Int = 0,
    var victories: Int = 0,
)

/**
 * Regras do placar de truco, sem dependência de Android.
 */
class TrucoGame(val playerOne: Player, val playerTwo: Player) {

    companion object {
        const val WINNING_SCORE = 12
    }

    val players get() = listOf(playerOne, playerTwo)

    /**
     * Soma [points] ao [player]. Se ele chegar a [WINNING_SCORE], ganha uma vitória,
     * os pontos dos dois são zerados e o vencedor é retornado; caso contrário, retorna null.
     */
    fun addPoints(player: Player, points: Int): Player? {
        require(player === playerOne || player === playerTwo) { "Jogador não pertence à partida" }
        require(points > 0) { "Pontos devem ser positivos" }

        player.points += points
        if (player.points < WINNING_SCORE) return null

        player.victories++
        resetPoints()
        return player
    }

    fun resetPoints() {
        players.forEach { it.points = 0 }
    }

    fun restartHistory() {
        players.forEach { it.victories = 0 }
        resetPoints()
    }

    /** Troca o nome apenas se [name] tiver conteúdo. */
    fun rename(player: Player, name: String?) {
        if (!name.isNullOrBlank()) player.name = name.trim()
    }
}
