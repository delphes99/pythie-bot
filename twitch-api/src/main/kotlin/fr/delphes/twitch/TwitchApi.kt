package fr.delphes.twitch

import fr.delphes.twitch.model.Game
import fr.delphes.twitch.model.GameId
import fr.delphes.twitch.model.Stream

interface TwitchApi {
    val userId: String

    suspend fun getStream(userId: String): Stream?

    suspend fun getGame(id: GameId): Game
}