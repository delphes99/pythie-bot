package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.UserName
import fr.delphes.twitch.irc.IrcChannelMessage

class ChannelMessageMapper(
    private val channel: TwitchChannel,
    private val connector: TwitchConnector
) : TwitchIncomingEventMapper<IrcChannelMessage> {
    override suspend fun handle(
        twitchEvent: IrcChannelMessage
    ): List<TwitchIncomingEvent> {
        val user = UserName(twitchEvent.user.name)
        val message = twitchEvent.message

        return if (twitchEvent.isFor(channel)) {
            val command = connector.commandsFor(channel).find { it.triggerMessage == message || message.startsWith("${it.triggerMessage} ") }

            listOfNotNull(
                when {
                    command != null -> {
                        val parameters = if (message.startsWith("${command.triggerMessage} ")) {
                            message.split(" ").drop(1)
                        } else {
                            emptyList()
                        }
                        CommandAsked(channel, command, user, parameters)
                    }
                    TwitchChannel(user.name) != connector.botAccount -> {
                        MessageReceived(channel, twitchEvent)
                    }
                    else -> {
                        null
                    }
                }
            )
        } else {
            emptyList()
        }
    }
}