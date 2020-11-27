package fr.delphes.bot.event.incoming

import fr.delphes.twitch.api.user.User
import fr.delphes.bot.command.Command

data class CommandAsked(
    val command: Command,
    val by: User
) : IncomingEvent