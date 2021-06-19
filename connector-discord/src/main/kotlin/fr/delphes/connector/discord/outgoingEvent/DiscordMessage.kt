package fr.delphes.connector.discord.outgoingEvent

import fr.delphes.connector.discord.DiscordConnector

data class DiscordMessage(val text: String, val channel: ChannelId) : DiscordOutgoingEvent {
    override suspend fun executeOnDiscord(client: DiscordConnector) {
        client.connected {
            val channel = this@connected.client.getTextChannelById(channel)!!
            channel.sendMessage(text).complete()
        }
    }
}