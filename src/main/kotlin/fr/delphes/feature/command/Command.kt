package fr.delphes.feature.command

import fr.delphes.bot.command.Command
import fr.delphes.bot.command.SimpleCommand
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.time.Clock
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.AbstractFeature
import fr.delphes.bot.time.SystemClock
import java.time.Duration
import java.time.LocalDateTime

class Command(
    trigger: String,
    lastActivation: LocalDateTime = LocalDateTime.MIN,
    clock: Clock = SystemClock,
    cooldown: Duration? = null,
    responses: List<OutgoingEvent>
) : AbstractFeature() {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(command)
    }

    private val command = SimpleCommand(
        trigger,
        lastActivation,
        clock,
        cooldown,
        responses
    )

    override val commands: Iterable<Command> = listOf(command)
}