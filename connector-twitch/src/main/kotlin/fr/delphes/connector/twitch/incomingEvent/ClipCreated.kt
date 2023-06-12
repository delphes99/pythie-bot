package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.annotation.RegisterIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.clips.Clip
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
data class ClipCreated(
    override val channel: TwitchChannel,
    val clip: Clip,
) : TwitchIncomingEvent
