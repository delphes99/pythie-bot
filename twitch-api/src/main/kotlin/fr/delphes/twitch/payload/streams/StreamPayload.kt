package fr.delphes.twitch.payload.streams

import kotlinx.serialization.Serializable

@Serializable
data class StreamPayload(
     val data: List<StreamInfos>
)