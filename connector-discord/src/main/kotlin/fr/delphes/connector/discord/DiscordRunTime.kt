package fr.delphes.connector.discord

import dev.kord.core.Kord
import fr.delphes.bot.connector.ConnectorRuntime

class DiscordRunTime(
    val client: Kord
): ConnectorRuntime {
    override suspend fun kill() {
        client.logout()
    }
}