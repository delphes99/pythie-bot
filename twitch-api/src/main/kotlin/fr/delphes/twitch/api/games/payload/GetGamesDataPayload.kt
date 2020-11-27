package fr.delphes.twitch.api.games.payload

import kotlinx.serialization.Serializable

@Serializable
data class GetGamesDataPayload(
    val box_art_url: String,
    val id: String,
    val name: String
)