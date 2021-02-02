package fr.delphes.bot.command

import fr.delphes.bot.ClientBot
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import java.time.Duration
import java.time.LocalDateTime

class SimpleCommandHandler(
    val command: Command,
    private var lastActivation: LocalDateTime = LocalDateTime.MIN,
    private val clock: Clock = SystemClock,
    private val cooldown: Duration? = null,
    private val responses: List<OutgoingEvent>
) : EventHandler<CommandAsked> {
    override suspend fun handle(event: CommandAsked, bot: ClientBot): List<OutgoingEvent> {
        return if(event.command == command &&
            cooldown?.let { Duration.between(lastActivation, clock.now()) > it } != false
        ) {
            lastActivation = clock.now()
            responses
        } else {
            emptyList()
        }
    }
}