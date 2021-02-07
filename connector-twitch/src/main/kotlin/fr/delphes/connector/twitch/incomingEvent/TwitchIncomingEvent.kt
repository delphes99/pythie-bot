package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.twitch.TwitchChannel

interface TwitchIncomingEvent : IncomingEvent {
    val channel: TwitchChannel

    fun isFor(channel: TwitchChannel) : Boolean {
        return this.channel == channel
    }

    suspend fun isFor(
        channel: TwitchChannel,
        transform: suspend () -> List<OutgoingEvent>
    ): List<OutgoingEvent> {
        return if(isFor(channel)) {
            transform()
        } else {
            emptyList()
        }
    }
}