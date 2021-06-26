package fr.delphes.connector.twitch.state

import fr.delphes.twitch.api.games.GameId
import kotlinx.serialization.Serializable

@Serializable
data class GameInfo(
    val id: GameId,
    val label: String
)