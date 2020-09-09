package fr.delphes.feature.command

import fr.delphes.time.Clock
import fr.delphes.event.eventHandler.EventHandler
import fr.delphes.event.incoming.MessageReceived
import fr.delphes.event.outgoing.OutgoingEvent
import fr.delphes.event.outgoing.SendMessage
import fr.delphes.feature.AbstractFeature
import fr.delphes.time.SystemClock
import java.time.Duration
import java.time.LocalDateTime

class Command(
    private val trigger: String,
    private val response: String,
    private var lastActivation: LocalDateTime = LocalDateTime.MIN,
    private val clock: Clock = SystemClock,
    private val cooldown: Duration? = null
) : AbstractFeature() {
    override val messageReceivedHandlers: List<EventHandler<MessageReceived>> = listOf(CommandMessageReceivedHandler())

    inner class CommandMessageReceivedHandler: EventHandler<MessageReceived> {
        override fun handle(event: MessageReceived): List<OutgoingEvent> {
            return if(event.text == trigger && cooldown?.let { Duration.between(lastActivation, clock.now()) > it } != false) {
                lastActivation = clock.now()
                listOf(SendMessage(response))
            } else {
                emptyList()
            }
        }
    }
}