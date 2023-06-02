package fr.delphes.twitch.api.games

import kotlinx.serialization.Serializable

@Serializable
data class Game(
    val id: GameId,
    val label: String,
)