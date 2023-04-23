package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.bot.event.builder.OutgoingEventBuilder
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class SendMessage(
    val text: String,
    override val channel: TwitchChannel
) : TwitchChatOutgoingEvent {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        connector: TwitchConnector
    ) {
        chat.sendMessage(IrcChannel.of(channel), text)
    }

    @Serializable
    @SerialName("twitch-send-message")
    class Builder(
        val text: String,
        val channel: TwitchChannel
    ) : OutgoingEventBuilder {
        override fun build() = SendMessage(text, channel)
    }
}