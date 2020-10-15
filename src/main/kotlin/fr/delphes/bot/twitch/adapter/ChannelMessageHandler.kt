package fr.delphes.bot.twitch.adapter

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import fr.delphes.User
import fr.delphes.bot.Channel
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.MessageReceived
import fr.delphes.bot.twitch.TwitchIncomingEventHandler

class ChannelMessageHandler(private val channel: Channel) : TwitchIncomingEventHandler<ChannelMessageEvent> {
    override fun transform(twitchEvent: ChannelMessageEvent): List<IncomingEvent> {
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