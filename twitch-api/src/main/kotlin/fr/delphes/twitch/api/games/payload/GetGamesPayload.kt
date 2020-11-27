package fr.delphes.twitch.api.games.payload

import kotlinx.serialization.Serializable

@Serializable
data class GetGamesPayload(
    val data: List<GetGamesDataPayload>
)