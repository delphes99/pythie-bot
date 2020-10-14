package fr.delphes.feature

import fr.delphes.bot.command.Command

abstract class AbstractFeature : Feature {
    override val commands: Iterable<Command> = emptyList()
}