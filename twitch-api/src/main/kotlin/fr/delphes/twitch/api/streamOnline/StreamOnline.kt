package fr.delphes.twitch.api.streamOnline

import fr.delphes.twitch.TwitchChannel

data class StreamOnline(
    val channel: TwitchChannel,
    val type: StreamType
)