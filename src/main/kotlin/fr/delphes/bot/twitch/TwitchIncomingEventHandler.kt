package fr.delphes.bot.twitch

import fr.delphes.bot.event.incoming.IncomingEvent

interface TwitchIncomingEventHandler<T>  {
    fun transform(twitchEvent: T) : List<IncomingEvent>
}