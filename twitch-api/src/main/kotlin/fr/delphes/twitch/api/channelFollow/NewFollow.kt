package fr.delphes.twitch.api.channelFollow

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User

data class NewFollow(
    val channel: TwitchChannel,
    val follower: User
)