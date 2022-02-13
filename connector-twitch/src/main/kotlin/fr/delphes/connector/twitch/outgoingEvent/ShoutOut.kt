package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.user.UserInfos
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

data class ShoutOut(
    val user: User,
    override val channel: TwitchChannel,
    val text: (UserInfos) -> String,
): TwitchChatOutgoingEvent {
    override suspend fun executeOnTwitch(chat: IrcClient, connector: TwitchConnector) {
        val highlight = connector.getUser(user)

        if(highlight != null)  {
            chat.sendMessage(IrcChannel.of(channel), text(highlight))
        }
    }
}
