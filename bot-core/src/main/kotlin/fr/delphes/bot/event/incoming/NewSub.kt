package fr.delphes.bot.event.incoming

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User

data class NewSub(
    override val channel: TwitchChannel,
    val sub: User
) : TwitchIncomingEvent