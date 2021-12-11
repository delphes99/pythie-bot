package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.TwitchConnector
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannelMessage

class ChannelMessageMapper(
    private val channel: TwitchChannel,
    private val connector: TwitchConnector
) : TwitchIncomingEventMapper<IrcChannelMessage> {
    override suspend fun handle(
        twitchEvent: IrcChannelMessage
    ): List<TwitchIncomingEvent> {
        val user = User(twitchEvent.user.name)
        val message = twitchEvent.message

        return if (twitchEvent.isFor(channel)) {
            val command = connector.commandsFor(channel).find { it.triggerMessage == message }

            listOfNotNull(
                when {
                    command != null -> {
                        CommandAsked(channel, command, user)
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