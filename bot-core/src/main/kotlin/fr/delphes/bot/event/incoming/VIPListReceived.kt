package fr.delphes.bot.event.incoming

import fr.delphes.twitch.api.user.User

data class VIPListReceived(val vips: List<User>): IncomingEvent {
    constructor(vararg users: String) : this(listOf(*users).map(::User))
}