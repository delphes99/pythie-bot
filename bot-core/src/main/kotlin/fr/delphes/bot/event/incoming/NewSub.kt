package fr.delphes.bot.event.incoming

import fr.delphes.twitch.model.User
import fr.delphes.bot.webserver.payload.newSub.NewSubData

data class NewSub(
    val sub: User
) : IncomingEvent {
    constructor(payload: NewSubData) : this(User(payload.event_data.user_name))
}