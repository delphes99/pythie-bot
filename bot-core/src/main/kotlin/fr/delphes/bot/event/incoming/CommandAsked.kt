package fr.delphes.bot.event.incoming

import fr.delphes.twitch.api.user.User
import fr.delphes.bot.command.Command
import fr.delphes.twitch.TwitchChannel

data class CommandAsked(
    override val channel: TwitchChannel,
    val command: Command,
    val by: User
) : TwitchIncomingEvent