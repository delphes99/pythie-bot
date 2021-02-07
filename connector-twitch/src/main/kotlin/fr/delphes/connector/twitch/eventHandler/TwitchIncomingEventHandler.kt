package fr.delphes.connector.twitch.eventHandler

import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent

interface TwitchIncomingEventHandler<T> {
    suspend fun handle(twitchEvent: T): List<TwitchIncomingEvent>
}