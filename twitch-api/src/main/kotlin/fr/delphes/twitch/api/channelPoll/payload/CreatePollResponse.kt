package fr.delphes.twitch.api.channelPoll.payload

import kotlinx.serialization.Serializable

@Serializable
data class CreatePollResponse(
    val data: List<CreatePollDataPayload>
)