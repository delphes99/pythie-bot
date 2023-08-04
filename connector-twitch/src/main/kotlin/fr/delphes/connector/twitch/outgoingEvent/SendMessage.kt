package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
import fr.delphes.annotation.outgoingEvent.builder.FieldDescription
import fr.delphes.annotation.outgoingEvent.builder.FieldMapper
import fr.delphes.bot.event.outgoing.LegacyOutgoingEventBuilder
import fr.delphes.bot.event.outgoing.WithBuilder
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.outgoingEvent.descriptionMapper.TwitchChannelMapper
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.feature.OutgoingEventType
import fr.delphes.feature.descriptor.StringFeatureDescriptor
import fr.delphes.state.StateManager
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@RegisterOutgoingEvent("twitch-send-message")
data class SendMessage(
    @FieldDescription("description of text")
    val text: String,
    @FieldMapper(TwitchChannelMapper::class)
    @FieldDescription("description of channel")
    override val channel: TwitchChannel,
) : TwitchChatOutgoingEvent {
    override suspend fun executeOnTwitch(
        chat: IrcClient,
        connector: TwitchConnector,
    ) {
        chat.sendMessage(IrcChannel.of(channel), text)
    }

    companion object : WithBuilder {
        override val type = OutgoingEventType("twitch-send-message")

        override val builderDefinition = buildDefinition(::Builder)

        @Serializable
        @SerialName("twitch-send-message")
        class Builder(
            val text: String = "",
            val channel: TwitchChannel = TwitchChannel(""),
        ) : LegacyOutgoingEventBuilder {
            override suspend fun build(stateManager: StateManager) = SendMessage(text, channel)

            override fun description(): OutgoingEventBuilderDescription {
                return buildDescription(
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
            }
        }
    }
}