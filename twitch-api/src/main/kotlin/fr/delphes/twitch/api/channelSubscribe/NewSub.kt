package fr.delphes.twitch.api.channelSubscribe

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName

data class NewSub(
    val channel: TwitchChannel,
    val user: UserName,
    val tier: SubscribeTier,
    val gift: Boolean
)