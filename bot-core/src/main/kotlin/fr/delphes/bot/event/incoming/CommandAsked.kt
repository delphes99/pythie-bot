package fr.delphes.bot.event.incoming

import fr.delphes.twitch.model.User
import fr.delphes.bot.command.Command

data class CommandAsked(
    val command: Command,
    val by: User
) : IncomingEvent