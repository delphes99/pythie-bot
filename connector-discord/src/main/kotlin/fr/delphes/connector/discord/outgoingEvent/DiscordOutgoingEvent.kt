package fr.delphes.connector.discord.outgoingEvent

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.DiscordConnector

sealed interface DiscordOutgoingEvent : OutgoingEvent {
    suspend fun executeOnDiscord(client: DiscordConnector)
}