package fr.delphes.twitch.payload.games

import kotlinx.serialization.Serializable

@Serializable
data class GetGamesDataPayload(
    val box_art_url: String,
    val id: String,
    val name: String
)