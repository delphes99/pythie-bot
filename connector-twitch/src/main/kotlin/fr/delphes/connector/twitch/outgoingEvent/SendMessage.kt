package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.FieldDescription
import fr.delphes.annotation.dynamicForm.FieldMapper
import fr.delphes.annotation.outgoingEvent.RegisterOutgoingEvent
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.outgoingEvent.descriptionMapper.TwitchChannelMapper
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcChannel
import fr.delphes.twitch.irc.IrcClient

@RegisterOutgoingEvent("twitch-send-message")
@DynamicForm("twitch-send-message-form", ["outgoing-event"])
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