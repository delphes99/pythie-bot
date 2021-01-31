package fr.delphes.features.twitch.command

import fr.delphes.bot.command.Command
import fr.delphes.bot.command.SimpleCommandHandler
import fr.delphes.bot.event.eventHandler.EventHandlers
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.TwitchFeature
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import java.time.Duration
import java.time.LocalDateTime

class Command(
    channel: String,
    trigger: String,
    lastActivation: LocalDateTime = LocalDateTime.MIN,
    clock: Clock = SystemClock,
    cooldown: Duration? = null,
    responses: List<OutgoingEvent>
) : TwitchFeature(channel) {
    override fun registerHandlers(eventHandlers: EventHandlers) {
        eventHandlers.addHandler(commandHandler)
    }

    private val commandHandler = SimpleCommandHandler(
        Command(trigger),
        lastActivation,
        clock,
        cooldown,
        responses
    )

    override val commands: Iterable<Command> = listOf(commandHandler.command)
}