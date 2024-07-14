package fr.delphes.connector.twitch.outgoingEvent

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.twitch.TwitchChannel
import kotlinx.serialization.Serializable

@Serializable
sealed interface TwitchOutgoingEvent : OutgoingEvent {
    val channel: TwitchChannel
}