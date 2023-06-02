package fr.delphes.connector.twitch.incomingEvent

import kotlinx.serialization.Serializable

@Serializable
data class PollVote(
    val nbVoteTotal: Long,
    val nbVoteByChannelPoints: Long,
    val nbVoteByCheer: Long,
)
