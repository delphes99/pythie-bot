package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.clips.Clip

data class ClipCreated(
    override val channel: TwitchChannel,
    val clip: Clip
): TwitchIncomingEvent
