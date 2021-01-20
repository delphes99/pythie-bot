package fr.delphes.connector.discord

import kotlinx.serialization.Serializable

@Serializable
data class DiscordConfiguration(
    val oAuthToken: String
) {
    companion object {
        fun empty(): DiscordConfiguration {
            return DiscordConfiguration("")
        }
    }
}