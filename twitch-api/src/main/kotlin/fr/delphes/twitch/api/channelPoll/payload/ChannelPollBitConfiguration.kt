package fr.delphes.twitch.api.channelPoll.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelPollBitConfiguration(
    val is_enabled: Boolean,
    val amount_per_vote: Int
)
