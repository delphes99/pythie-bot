package fr.delphes.connector.twitch.incomingEvent

import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.twitch.TwitchChannel

interface TwitchIncomingEvent : IncomingEvent {
    val channel: TwitchChannel

    fun isFor(channel: TwitchChannel) : Boolean {
        //TODO normalize twitch channel name
        return this.channel.name.equals(channel.name, true)
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