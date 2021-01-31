package fr.delphes.bot.event.incoming

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User

data class NewFollow(
    override val channel: TwitchChannel,
    val follower: User
) : TwitchIncomingEvent