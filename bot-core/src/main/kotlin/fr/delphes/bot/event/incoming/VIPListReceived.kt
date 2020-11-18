package fr.delphes.bot.event.incoming

import fr.delphes.twitch.model.User

data class VIPListReceived(val vips: List<User>): IncomingEvent {
    constructor(vararg users: String) : this(listOf(*users).map(::User))
}