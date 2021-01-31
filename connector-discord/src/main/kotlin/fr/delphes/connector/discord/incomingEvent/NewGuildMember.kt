package fr.delphes.connector.discord.incomingEvent

import fr.delphes.bot.event.incoming.IncomingEvent

data class NewGuildMember(
    val user: String
) : IncomingEvent