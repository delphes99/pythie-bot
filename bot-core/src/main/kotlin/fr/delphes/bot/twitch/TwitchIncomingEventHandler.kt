package fr.delphes.bot.twitch

import fr.delphes.bot.event.incoming.TwitchIncomingEvent

interface TwitchIncomingEventHandler<T> {
    suspend fun handle(twitchEvent: T): List<TwitchIncomingEvent>
}