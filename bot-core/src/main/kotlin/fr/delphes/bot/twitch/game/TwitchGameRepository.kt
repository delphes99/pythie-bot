package fr.delphes.bot.twitch.game

import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId
import kotlinx.coroutines.runBlocking

class TwitchGameRepository(
    private val getGame: suspend (GameId) -> Game
): GameRepository {
    override fun get(gameId: GameId): Game {
        return runBlocking { getGame(gameId) }
    }
}