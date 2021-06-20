package fr.delphes.connector.twitch.eventMapper

import fr.delphes.bot.state.UserMessage
import fr.delphes.connector.twitch.ClientBot
import fr.delphes.connector.twitch.incomingEvent.CommandAsked
import fr.delphes.connector.twitch.incomingEvent.MessageReceived
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.connector.twitch.state.BotAccountProvider
import fr.delphes.connector.twitch.state.action.MessageReceivedAction
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.api.user.User
import fr.delphes.twitch.irc.IrcChannelMessage
import fr.delphes.utils.store.Action

class ChannelMessageMapper(
    private val channel: TwitchChannel,
    private val bot: ClientBot,
    private val getBotAccountProvider: BotAccountProvider = bot.connector.technicalState,
    private val applyAction: (Action) -> Unit = { action -> bot.bot.applyAction(action) }
) : TwitchIncomingEventMapper<IrcChannelMessage> {
    override suspend fun handle(
        twitchEvent: IrcChannelMessage
    ): List<TwitchIncomingEvent> {
        val user = User(twitchEvent.user.name)
        val message = twitchEvent.message

        return if (twitchEvent.isFor(channel)) {
            val command = bot.commandsFor(channel).find { it.triggerMessage == message }

            listOfNotNull(
                when {
                    command != null -> {
                        CommandAsked(channel, command, user)
                    }
                    TwitchChannel(user.name) != getBotAccountProvider.botAccount -> {
                        applyAction(MessageReceivedAction(channel, user, message))
                        //TODO DELETE
                        bot.channelOf(twitchEvent.channel.toTwitchChannel())?.state?.addMessage(
                            UserMessage(
                                user,
                                message
                            )
                        )
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