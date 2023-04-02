package fr.delphes.twitch.api.channelRaid

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName

data class IncomingRaid(
    val channel: TwitchChannel,
    val from: UserName,
    val viewers: Long
)
