package fr.delphes.feature

import fr.delphes.bot.command.Command

abstract class TwitchFeature(
    //TODO type
    val channel: String
) : Feature {
    open val commands: Iterable<Command> = emptyList()
}