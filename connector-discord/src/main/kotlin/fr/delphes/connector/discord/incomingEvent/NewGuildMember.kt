package fr.delphes.connector.discord.incomingEvent

import fr.delphes.annotation.incomingEvent.RegisterIncomingEvent
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
data class NewGuildMember(
    val user: String,
) : DiscordIncomingEvent