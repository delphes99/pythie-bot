package fr.delphes.twitch.api.channelCheer

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User

data class NewCheer(
    val channel: TwitchChannel,
    val cheerer: User?,
    val bits: Long,
    val message: String?
)