package fr.delphes.bot.twitch.handler

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import fr.delphes.User
import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.MessageReceived
import fr.delphes.bot.state.ChannelChangeState
import fr.delphes.bot.twitch.TwitchIncomingEventHandler

class ChannelMessageHandler : TwitchIncomingEventHandler<ChannelMessageEvent> {
    override fun handle(
        twitchEvent: ChannelMessageEvent,
        channel: ChannelInfo,
        changeState: ChannelChangeState
    ): List<IncomingEvent> {
        val command = channel.commands.find { it.triggerMessage == twitchEvent.message }

        return listOf(
            if (command != null) {
                CommandAsked(command, User(twitchEvent.user.name))
            } else {
                MessageReceived(twitchEvent)
            }
        )
    }
}