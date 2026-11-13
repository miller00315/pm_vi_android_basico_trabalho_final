package com.miller.oliveira.marcatruco

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test

class TrucoGameTest {

    private lateinit var playerOne: Player
    private lateinit var playerTwo: Player
    private lateinit var game: TrucoGame

    @Before
    fun setUp() {
        playerOne = Player("Jogador 1")
        playerTwo = Player("Jogador 2")
        game = TrucoGame(playerOne, playerTwo)
    }

    @Test
    fun `novo jogo comeca zerado`() {
        game.players.forEach {
            assertEquals(0, it.points)
            assertEquals(0, it.victories)
        }
    }

    @Test
    fun `addPoints soma apenas no jogador informado`() {
        val winner = game.addPoints(playerOne, 3)

        assertNull(winner)
        assertEquals(3, playerOne.points)
        assertEquals(0, playerTwo.points)
    }

    @Test
    fun `addPoints acumula pontos`() {
        game.addPoints(playerTwo, 1)
        game.addPoints(playerTwo, 3)
        game.addPoints(playerTwo, 6)

        assertEquals(10, playerTwo.points)
    }

    @Test
    fun `abaixo de 12 nao ha vencedor`() {
        val winner = game.addPoints(playerOne, TrucoGame.WINNING_SCORE - 1)

        assertNull(winner)
        assertEquals(11, playerOne.points)
        assertEquals(0, playerOne.victories)
    }

    @Test
    fun `chegar exatamente a 12 da vitoria e zera os pontos dos dois`() {
        game.addPoints(playerTwo, 5)
        game.addPoints(playerOne, 9)

        val winner = game.addPoints(playerOne, 3)

        assertSame(playerOne, winner)
        assertEquals(1, playerOne.victories)
        assertEquals(0, playerTwo.victories)
        assertEquals(0, playerOne.points)
        assertEquals(0, playerTwo.points)
    }

    @Test
    fun `passar de 12 tambem conta como vitoria`() {
        game.addPoints(playerTwo, 11)

        val winner = game.addPoints(playerTwo, 6)

        assertSame(playerTwo, winner)
        assertEquals(1, playerTwo.victories)
        assertEquals(0, playerTwo.points)
    }

    @Test
    fun `vitorias se acumulam entre partidas`() {
        game.addPoints(playerTwo, 12)
        game.addPoints(playerTwo, 12)
        game.addPoints(playerOne, 12)

        assertEquals(1, playerOne.victories)
        assertEquals(2, playerTwo.victories)
    }

    @Test
    fun `resetPoints zera pontos e mantem vitorias`() {
        game.addPoints(playerOne, 12)
        game.addPoints(playerOne, 4)
        game.addPoints(playerTwo, 7)

        game.resetPoints()

        assertEquals(0, playerOne.points)
        assertEquals(0, playerTwo.points)
        assertEquals(1, playerOne.victories)
    }

    @Test
    fun `restartHistory zera pontos e vitorias`() {
        game.addPoints(playerOne, 12)
        game.addPoints(playerTwo, 12)
        game.addPoints(playerTwo, 5)

        game.restartHistory()

        game.players.forEach {
            assertEquals(0, it.points)
            assertEquals(0, it.victories)
        }
    }

    @Test
    fun `rename troca o nome removendo espacos`() {
        game.rename(playerOne, "  Ana  ")

        assertEquals("Ana", playerOne.name)
        assertEquals("Jogador 2", playerTwo.name)
    }

    @Test
    fun `rename ignora nome nulo vazio ou em branco`() {
        game.rename(playerOne, null)
        game.rename(playerOne, "")
        game.rename(playerOne, "   ")

        assertEquals("Jogador 1", playerOne.name)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `addPoints recusa jogador de fora da partida`() {
        game.addPoints(Player("Intruso"), 1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `addPoints recusa pontos zero`() {
        game.addPoints(playerOne, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `addPoints recusa pontos negativos`() {
        game.addPoints(playerOne, -3)
    }
}
