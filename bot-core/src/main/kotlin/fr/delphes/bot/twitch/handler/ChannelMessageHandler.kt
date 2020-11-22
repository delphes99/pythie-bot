package fr.delphes.bot.twitch.handler

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import fr.delphes.twitch.model.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.MessageReceived
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.state.UserMessage
import fr.delphes.bot.twitch.TwitchIncomingEventHandler

class ChannelMessageHandler : TwitchIncomingEventHandler<ChannelMessageEvent> {
    override fun handle(
        twitchEvent: ChannelMessageEvent,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        val user = User(twitchEvent.user.name)
        val message = twitchEvent.message

        val command = channel.commands.find { it.triggerMessage == message }

        return listOf(
            if (command != null) {
                CommandAsked(command, user)
            } else {
                changeState.addMessage(UserMessage(user, message))
                MessageReceived(twitchEvent)
            }
        )
    }
}