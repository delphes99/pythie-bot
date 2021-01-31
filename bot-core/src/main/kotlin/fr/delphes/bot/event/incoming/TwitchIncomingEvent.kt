package fr.delphes.bot.event.incoming

import fr.delphes.twitch.TwitchChannel

interface TwitchIncomingEvent : IncomingEvent {
    val channel: TwitchChannel
}