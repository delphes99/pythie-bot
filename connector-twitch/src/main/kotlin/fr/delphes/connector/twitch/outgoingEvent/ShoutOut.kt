package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.user.UserInfos
import fr.delphes.twitch.ChannelTwitchApi
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.channel.ChannelInformation
import fr.delphes.twitch.api.user.UserName
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

data class ShoutOut(
    val user: UserName,
    override val channel: TwitchChannel,
    val text: (UserInfos, ChannelInformation?) -> String,
) : TwitchChatOutgoingEvent, TwitchApiOutgoingEvent {
    override suspend fun executeOnTwitch(chat: IrcClient, connector: TwitchConnector) {
        connector.getUser(user)?.let { userToShoutout ->
            val channelInformation = connector.getChannelInformation(userToShoutout.id)

            if (channel.normalizeName != user.normalizeName) {
                chat.sendMessage(IrcChannel.of(channel), text(userToShoutout, channelInformation))
            }
        }
    }

    override suspend fun executeOnTwitch(twitchApi: ChannelTwitchApi, connector: TwitchConnector) {
        if (channel.normalizeName != user.normalizeName) {
            connector.getUser(user)?.id?.let { id ->
                twitchApi.sendShoutout(id)
            }
        }
    }
}
