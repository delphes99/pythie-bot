package fr.delphes.connector.twitch.builder

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.connector.twitch.outgoingEvent.SendMessage
import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("twitch-send-message")
data class SendMessageBuilder(
    val text: String,
    val channel: String,
) : OutgoingEventBuilder {
    override fun build() = SendMessage(text, TwitchChannel(channel))
}