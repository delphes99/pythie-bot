package fr.delphes.event.incoming

import fr.delphes.User
import fr.delphes.bot.webserver.payload.newSub.NewSubData

data class NewSub(
    val sub: User
) : IncomingEvent {
    constructor(payload: NewSubData) : this(User(payload.event_data.user_name))
}