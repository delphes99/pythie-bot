package fr.delphes.bot.command

import fr.delphes.bot.ChannelInfo
import fr.delphes.bot.event.eventHandler.EventHandler
import fr.delphes.bot.event.incoming.CommandAsked
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.bot.util.time.Clock
import fr.delphes.bot.util.time.SystemClock
import java.time.Duration
import java.time.LocalDateTime

class SimpleCommand(
    override val triggerMessage: String,
    private var lastActivation: LocalDateTime = LocalDateTime.MIN,
    private val clock: Clock = SystemClock,
    private val cooldown: Duration? = null,
    private val responses: List<OutgoingEvent>
) : Command, EventHandler<CommandAsked> {
    override fun handle(event: CommandAsked, channel: ChannelInfo): List<OutgoingEvent> {
        return if(event.command == this && cooldown?.let { Duration.between(lastActivation, clock.now()) > it } != false) {
            lastActivation = clock.now()
            responses
        } else {
            emptyList()
        }
    }
}