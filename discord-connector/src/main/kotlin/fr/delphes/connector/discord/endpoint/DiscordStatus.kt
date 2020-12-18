package fr.delphes.connector.discord.endpoint

import kotlinx.serialization.Serializable

@Serializable
data class DiscordStatus(
    val status: DiscordStatusEnum
)