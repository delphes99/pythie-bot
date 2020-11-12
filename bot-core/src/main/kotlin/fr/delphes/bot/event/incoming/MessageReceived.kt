package fr.delphes.bot.event.incoming

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import fr.delphes.User

data class MessageReceived(
    val user: User,
    val text: String
) : IncomingEvent {
    constructor(event: ChannelMessageEvent) : this(event.user.name, event.message)

    constructor(
        user: String,
        text: String
    ) : this(User(user), text)
}