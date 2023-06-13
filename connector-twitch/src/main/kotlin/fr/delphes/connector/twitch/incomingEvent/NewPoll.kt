package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.annotation.incomingEvent.RegisterIncomingEvent
import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
data class NewPoll(
    override val channel: TwitchChannel,
    val poll: Poll,
) : TwitchIncomingEvent
