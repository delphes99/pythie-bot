package fr.delphes.connector.twitch

import fr.delphes.bot.Bot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.twitch.TwitchChannel

abstract class TwitchEventHandler<T: TwitchIncomingEvent>(
    private val channel: TwitchChannel
): EventHandler<T> {
    override suspend fun handle(event: T, bot: Bot): List<OutgoingEvent> {
        return event.isFor(channel) {
            handleIfGoodChannel(event, bot)
        }
    }

    abstract suspend fun handleIfGoodChannel(event: T, bot: Bot) : List<OutgoingEvent>
}