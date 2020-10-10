package fr.delphes.bot.command

import fr.delphes.User
import fr.delphes.bot.Channel
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.time.Clock
import fr.delphes.bot.time.SystemClock
import java.time.Duration
import java.time.LocalDateTime

class SimpleCommand(
    override val triggerMessage: String,
    private var lastActivation: LocalDateTime = LocalDateTime.MIN,
    private val clock: Clock = SystemClock,
    private val cooldown: Duration? = null,
    private val responses: List<OutgoingEvent>
) : Command {
    override fun execute(user: User, channel: Channel): List<OutgoingEvent> {
        return if(cooldown?.let { Duration.between(lastActivation, clock.now()) > it } != false) {
            lastActivation = clock.now()
            responses
        } else {
            emptyList()
        }
    }
}