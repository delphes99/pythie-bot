package fr.delphes.bot.twitch.game

import fr.delphes.twitch.model.Game
import fr.delphes.twitch.model.GameId

interface GameRepository {
    fun get(gameId: GameId) : Game
}