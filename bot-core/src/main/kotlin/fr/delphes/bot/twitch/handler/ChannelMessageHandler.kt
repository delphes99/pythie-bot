package fr.delphes.bot.twitch.handler

import fr.delphes.twitch.api.user.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.MessageReceived
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.state.UserMessage
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcChannelMessage

class ChannelMessageHandler(
    private val channel: TwitchChannel
) : TwitchIncomingEventHandler<IrcChannelMessage> {
    override suspend fun handle(
        twitchEvent: IrcChannelMessage,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        val user = User(twitchEvent.user.name)
        val message = twitchEvent.message

        val command = channel.commands.find { it.triggerMessage == message }

        return listOf(
            if (command != null) {
                CommandAsked(this@ChannelMessageHandler.channel, command, user)
            } else {
                changeState.addMessage(UserMessage(user, message))
                MessageReceived(this@ChannelMessageHandler.channel, twitchEvent)
            }
        )
    }
}