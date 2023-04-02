package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import fr.delphes.twitch.irc.IrcChannelMessage

data class MessageReceived(
    override val channel: TwitchChannel,
    val user: UserName,
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
    ) : this(channel, UserName(user), text)
}