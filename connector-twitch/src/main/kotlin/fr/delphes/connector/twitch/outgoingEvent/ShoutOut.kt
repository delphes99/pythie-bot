package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.user.UserInfos
import fr.delphes.connector.twitch.user.toTwitchUser
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channel.ChannelInformation
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

data class ShoutOut(
    val user: User,
    override val channel: TwitchChannel,
    val text: (UserInfos, ChannelInformation?) -> String,
) : TwitchChatOutgoingEvent, TwitchApiOutgoingEvent {
    override suspend fun executeOnTwitch(chat: IrcClient, connector: TwitchConnector) {
        val highlight = connector.getUser(user)
        val channelInformation = connector.getChannelInformation(user)

        if (highlight != null && channel.normalizeName != user.normalizeName) {
            chat.sendMessage(IrcChannel.of(channel), text(highlight, channelInformation))
        }
    }

    override suspend fun executeOnTwitch(twitchApi: ChannelTwitchApi, connector: TwitchConnector) {
        if (channel.normalizeName != user.normalizeName) {
            connector.getUser(user)?.toTwitchUser()?.let { user ->
                twitchApi.sendShoutout(user)
            }
        }
    }
}
