package fr.delphes.connector.twitch.eventMapper

import fr.delphes.bot.state.UserMessage
import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannelMessage

class ChannelMessageMapper(
    private val channel: TwitchChannel,
    private val bot: ClientBot
) : TwitchIncomingEventMapper<IrcChannelMessage> {
    override suspend fun handle(
        twitchEvent: IrcChannelMessage
    ): List<TwitchIncomingEvent> {
        val user = User(twitchEvent.user.name)
        val message = twitchEvent.message

        return if(twitchEvent.isFor(channel)) {
            val command = bot.commandsFor(channel).find { it.triggerMessage == message }

            listOf(
                if (command != null) {
                    CommandAsked(channel, command, user)
                } else {
                    bot.channelOf(twitchEvent.channel.toTwitchChannel())?.state?.addMessage(UserMessage(user, message))
                    MessageReceived(channel, twitchEvent)
                }
            )
        } else {
            emptyList()
        }
    }
}