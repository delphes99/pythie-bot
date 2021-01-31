package fr.delphes.bot.event.incoming

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User

data class VIPListReceived(
    override val channel: TwitchChannel,
    val vips: List<User>
) : TwitchIncomingEvent {
    constructor(
        channel: TwitchChannel,
        vararg users: String
    ) : this(channel, listOf(*users).map(::User))
}