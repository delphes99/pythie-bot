package fr.delphes.connector.twitch.eventMapper

import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent

interface TwitchIncomingEventMapper<T> {
    suspend fun handle(twitchEvent: T): List<TwitchIncomingEvent>
}