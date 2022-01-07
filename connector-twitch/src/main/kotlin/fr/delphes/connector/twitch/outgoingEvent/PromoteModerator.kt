package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

data class PromoteModerator(
    val user: User,
    override val channel: TwitchChannel
) : TwitchOwnerChatOutgoingEvent {
    constructor(
        user: String,
        channel: TwitchChannel
    ) : this(User(user), channel)

    override suspend fun executeOnTwitch(
        chat: IrcClient,
    ) {
        chat.sendMessage(IrcChannel.of(channel), "/mod $user")
    }
}