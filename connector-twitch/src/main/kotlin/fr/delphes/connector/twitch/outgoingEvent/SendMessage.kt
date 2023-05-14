package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.bot.event.builder.OutgoingEventBuilder
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.state.StateManager
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class SendMessage(
    val text: String,
    override val channel: TwitchChannel,
) : TwitchChatOutgoingEvent {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        connector: TwitchConnector,
    ) {
        chat.sendMessage(IrcChannel.of(channel), text)
    }

    @Serializable
    @SerialName("twitch-send-message")
    class Builder(
        val text: String = "",
        val channel: TwitchChannel = TwitchChannel(""),
    ) : OutgoingEventBuilder {
        override suspend fun build(stateManager: StateManager) = SendMessage(text, channel)

        override fun description(): OutgoingEventBuilderDescription {
            return OutgoingEventBuilderDescription(
                type = "twitch-send-message",
                descriptors = listOf(
                    StringFeatureDescriptor(
                        fieldName = "text",
                        description = "description of text",
                        value = text
                    ),
                    StringFeatureDescriptor(
                        fieldName = "channel",
                        description = "description of channel",
                        value = channel.name
                    )
                )
            )
        }
    }
}