package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel

data class PollClosed(
    override val channel: TwitchChannel,
    val poll: Poll,
    val votes: Map<PollChoice, PollVote>
) : TwitchIncomingEvent
