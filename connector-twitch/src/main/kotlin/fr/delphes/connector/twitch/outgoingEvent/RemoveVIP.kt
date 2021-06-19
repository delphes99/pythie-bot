package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.Channel
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

data class RemoveVIP(
    val user: User,
    override val channel: TwitchChannel
) : TwitchOutgoingEvent {
    constructor(
        user: String,
        channel: TwitchChannel
    ) : this(User(user), channel)

    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        ownerChat.sendMessage(IrcChannel.of(channel.channel), "/unvip $user")
    }
}