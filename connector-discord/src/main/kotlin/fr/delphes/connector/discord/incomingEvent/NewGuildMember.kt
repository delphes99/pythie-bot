package fr.delphes.connector.discord.incomingEvent

import fr.delphes.bot.event.incoming.IncomingEvent
import kotlinx.serialization.Serializable

@Serializable
data class NewGuildMember(
    val user: String,
) : IncomingEvent