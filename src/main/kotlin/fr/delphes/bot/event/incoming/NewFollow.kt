package fr.delphes.bot.event.incoming

import fr.delphes.User
import fr.delphes.bot.webserver.payload.newFollow.NewFollowData

data class NewFollow(
    val follower: User
) : IncomingEvent {
    constructor(payload: NewFollowData) : this(User(payload.from_name))
}