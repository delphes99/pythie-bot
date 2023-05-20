package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.OutgoingEventProcessor
import fr.delphes.bot.event.incoming.IncomingEvent
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.state.State
import fr.delphes.state.StateId
import fr.delphes.state.StateManager

class EventHandlerParameters<T : IncomingEvent>(
    private val outgoingEventProcessor: OutgoingEventProcessor,
    val event: T,
    val stateManager: StateManager,
) {
    suspend fun executeOutgoingEvent(event: OutgoingEvent) {
        outgoingEventProcessor.processOutgoingEvent(event)
    }

    inline fun <reified T : State> state(stateId: StateId<T>) = stateManager.getState(stateId)
}