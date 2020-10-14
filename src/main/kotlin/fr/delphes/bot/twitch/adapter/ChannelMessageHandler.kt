package fr.delphes.bot.twitch.adapter

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.MessageReceived
import fr.delphes.bot.twitch.TwitchIncomingEventHandler

class ChannelMessageHandler : TwitchIncomingEventHandler<ChannelMessageEvent> {
    override fun transform(twitchEvent: ChannelMessageEvent): List<IncomingEvent> {
        return listOf(MessageReceived(twitchEvent))
    }
}