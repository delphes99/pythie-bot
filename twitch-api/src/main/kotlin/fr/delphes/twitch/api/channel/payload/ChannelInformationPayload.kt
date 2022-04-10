package fr.delphes.twitch.api.channel.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelInformationPayload(
    val data: List<ChannelInformation>
)
