package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.Serializable

@Serializable
data class PollUpdated(
    override val channel: TwitchChannel,
    val poll: Poll,
    val votes: Map<PollChoice, PollVote>,
) : TwitchIncomingEvent
