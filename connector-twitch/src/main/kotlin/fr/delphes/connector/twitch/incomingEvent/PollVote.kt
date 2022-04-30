package fr.delphes.connector.twitch.incomingEvent

data class PollVote(
    val nbVoteTotal: Long,
    val nbVoteByChannelPoints: Long,
    val nbVoteByCheer: Long
)
