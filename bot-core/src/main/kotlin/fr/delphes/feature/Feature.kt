package fr.delphes.feature

import fr.delphes.bot.command.Command
import fr.delphes.bot.event.eventHandler.EventHandlers

interface Feature {
    fun registerHandlers(eventHandlers: EventHandlers)

    val commands: Iterable<Command>
}