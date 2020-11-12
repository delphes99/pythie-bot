package fr.delphes.bot.event.incoming

import fr.delphes.User

data class VIPListReceived(val vips: List<User>): IncomingEvent {
    constructor(vararg users: String) : this(listOf(*users).map(::User))
}