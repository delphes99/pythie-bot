package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
import fr.delphes.annotation.outgoingEvent.createBuilder.FieldDescription
import fr.delphes.annotation.outgoingEvent.createBuilder.FieldMapper
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.outgoingEvent.descriptionMapper.TwitchChannelMapper
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

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
}