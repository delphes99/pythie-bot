package fr.delphes.connector.discord.outgoingEvent

import dev.kord.common.entity.Snowflake
import fr.delphes.connector.discord.DiscordConnector

data class DiscordMessage(val text: String, val channel: ChannelId) : DiscordOutgoingEvent {
    override suspend fun executeOnDiscord(client: DiscordConnector) {
        client.connected {
            this.client.rest.channel.createMessage(Snowflake(channel)) {
                content = text
            }
        }
    }
}