package fr.delphes.connector.discord.outgoingEvent

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.discord.Discord

sealed class DiscordOutgoingEvent: OutgoingEvent {
    abstract suspend fun executeOnDiscord(client: Discord)
}

typealias ChannelId = Long

data class DiscordMessage(val text: String, val channel: ChannelId): DiscordOutgoingEvent() {
    override suspend fun executeOnDiscord(client: Discord) {
        client.connected {
            send(text, channel)
        }
    }
}