package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.Serializable

@Serializable
data class StreamOffline(
    override val channel: TwitchChannel,
) : TwitchIncomingEvent