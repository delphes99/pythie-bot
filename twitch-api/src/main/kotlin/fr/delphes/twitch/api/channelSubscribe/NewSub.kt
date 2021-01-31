package fr.delphes.twitch.api.channelSubscribe

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User

data class NewSub(
    val channel: TwitchChannel,
    val user: User,
    val tier: SubscribeTier,
    val gift: Boolean
)