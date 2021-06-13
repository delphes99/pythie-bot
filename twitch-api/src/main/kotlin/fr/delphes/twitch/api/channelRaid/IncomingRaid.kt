package fr.delphes.twitch.api.channelRaid

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User

data class IncomingRaid(
    val channel: TwitchChannel,
    val from: User,
    val viewers: Long
)
