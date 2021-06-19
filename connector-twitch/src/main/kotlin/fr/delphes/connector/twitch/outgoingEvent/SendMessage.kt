package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.Channel
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

data class SendMessage(
    val text: String,
    override val channel: TwitchChannel
) : TwitchOutgoingEvent {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        ownerChat: IrcClient,
        twitchApi: ChannelTwitchApi,
        channel: Channel
    ) {
        chat.sendMessage(IrcChannel.of(channel.channel), text)
    }
}