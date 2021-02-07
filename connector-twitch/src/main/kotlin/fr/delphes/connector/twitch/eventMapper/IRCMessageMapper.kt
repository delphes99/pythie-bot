package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.connector.twitch.incomingEvent.VIPListReceived
import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.irc.IrcMessage

class IRCMessageMapper(
    private val channel: TwitchChannel
) : TwitchIncomingEventMapper<IrcMessage> {
    override suspend fun handle(
        twitchEvent: IrcMessage
    ): List<TwitchIncomingEvent> {
        val vipResult = VIPParser.extractVips(twitchEvent.message)
        return if (vipResult is VIPParser.VIPResult.VIPList) {
            listOf(VIPListReceived(this@IRCMessageMapper.channel, vipResult.users))
        } else {
            emptyList()
        }
    }
}