package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel

data class StreamOffline(
    override val channel: TwitchChannel
) : TwitchIncomingEvent