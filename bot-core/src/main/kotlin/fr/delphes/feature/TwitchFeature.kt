package fr.delphes.feature

import fr.delphes.bot.command.Command
import fr.delphes.twitch.TwitchChannel

abstract class TwitchFeature(
    val channel: TwitchChannel
) : Feature {
    open val commands: Iterable<Command> = emptyList()
}