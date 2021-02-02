package fr.delphes.bot.twitch.handler

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.incoming.MessageReceived
import fr.delphes.bot.event.incoming.TwitchIncomingEvent
import fr.delphes.bot.state.UserMessage
import fr.delphes.bot.twitch.TwitchIncomingEventHandler
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannelMessage

class ChannelMessageHandler(
    private val channel: TwitchChannel,
    private val bot: ClientBot
) : TwitchIncomingEventHandler<IrcChannelMessage> {
    override suspend fun handle(
        twitchEvent: IrcChannelMessage
    ): List<TwitchIncomingEvent> {
        val user = User(twitchEvent.user.name)
        val message = twitchEvent.message

        val command = bot.commandsFor(channel).find { it.triggerMessage == message }

        return listOf(
            if (command != null) {
                CommandAsked(channel, command, user)
            } else {
                bot.channelOf(twitchEvent.channel.toTwitchChannel())?.state?.addMessage(UserMessage(user, message))
                MessageReceived(channel, twitchEvent)
            }
        )
    }
}