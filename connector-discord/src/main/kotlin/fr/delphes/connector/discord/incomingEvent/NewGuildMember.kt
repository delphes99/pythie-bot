package fr.delphes.connector.discord.incomingEvent

import kotlinx.serialization.Serializable

@Serializable
data class NewGuildMember(
    val user: String,
) : DiscordIncomingEvent