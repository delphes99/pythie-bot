package fr.delphes.twitch.api.games

import kotlinx.serialization.Serializable

@Serializable
data class GameId(override val id: String) : WithGameId