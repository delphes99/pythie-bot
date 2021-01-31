package fr.delphes.bot.event.incoming

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannelMessage

data class MessageReceived(
    override val channel: TwitchChannel,
    val user: User,
    val text: String
) : TwitchIncomingEvent {
    constructor(
        channel: TwitchChannel,
        event: IrcChannelMessage
    ) : this(channel, event.user.name, event.message)

    constructor(
        channel: TwitchChannel,
        user: String,
        text: String
    ) : this(channel, User(user), text)
}