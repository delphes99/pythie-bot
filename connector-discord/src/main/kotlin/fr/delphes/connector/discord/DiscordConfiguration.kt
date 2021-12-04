package fr.delphes.connector.discord

import fr.delphes.bot.connector.ConnectorConfiguration
import kotlinx.serialization.Serializable

@Serializable
data class DiscordConfiguration(
    val oAuthToken: String
): ConnectorConfiguration {
    companion object {
        fun empty(): DiscordConfiguration {
            return DiscordConfiguration("")
        }
    }
}