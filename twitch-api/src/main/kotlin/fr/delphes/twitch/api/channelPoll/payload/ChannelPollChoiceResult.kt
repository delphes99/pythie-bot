package fr.delphes.twitch.api.channelPoll.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChannelPollChoiceResult(
    val id: String,
    val title: String,
    val bits_votes: Int = 0,
    val channel_points_votes: Int = 0,
    val votes: Int = 0
)
