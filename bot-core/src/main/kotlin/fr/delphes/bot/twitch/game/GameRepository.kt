package fr.delphes.bot.twitch.game

import fr.delphes.twitch.api.games.Game
import fr.delphes.twitch.api.games.GameId

interface GameRepository {
    fun get(gameId: GameId) : Game
}