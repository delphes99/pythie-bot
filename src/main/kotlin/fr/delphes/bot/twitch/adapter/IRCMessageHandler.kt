package fr.delphes.bot.twitch.adapter

import com.github.twitch4j.chat.events.channel.IRCMessageEvent
import fr.delphes.VIPParser
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.incoming.VIPListReceived
import fr.delphes.bot.twitch.TwitchIncomingEventHandler

class IRCMessageHandler: TwitchIncomingEventHandler<IRCMessageEvent> {
    override fun transform(twitchEvent: IRCMessageEvent): List<IncomingEvent> {
        return twitchEvent.message?.map { message ->
            val vipResult = VIPParser.extractVips(message)
            if (vipResult is VIPParser.VIPResult.VIPList) {
                listOf(VIPListReceived(vipResult.users))
            } else {
                emptyList()
            }
        }?.orElse(emptyList()) ?: emptyList()
    }
}