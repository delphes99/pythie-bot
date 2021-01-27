package fr.delphes.bot.event.incoming

import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannelMessage

data class MessageReceived(
    val user: User,
    val text: String
) : IncomingEvent {
    constructor(event: IrcChannelMessage) : this(event.user.name, event.message)

    constructor(
        user: String,
        text: String
    ) : this(User(user), text)
}