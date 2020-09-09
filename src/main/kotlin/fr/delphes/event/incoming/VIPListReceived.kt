package fr.delphes.event.incoming

import fr.delphes.User

data class VIPListReceived(val vips: List<User>): IncomingEvent {
    constructor(vararg users: String) : this(listOf(*users).map(::User))
}