package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

data class RetrieveVip(
    override val channel: TwitchChannel
) : TwitchOwnerChatOutgoingEvent {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
    ) {
        chat.sendMessage(IrcChannel.of(channel), "/vips")
    }
}