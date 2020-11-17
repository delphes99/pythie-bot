package fr.delphes.twitch.payload.games

import kotlinx.serialization.Serializable

@Serializable
data class GetGamesPayload(
    val data: List<GetGamesDataPayload>
)