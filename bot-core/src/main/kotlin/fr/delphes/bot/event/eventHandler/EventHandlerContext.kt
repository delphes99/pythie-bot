package fr.delphes.bot.event.eventHandler

import fr.delphes.bot.OutgoingEventProcessor
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.state.State
import fr.delphes.state.StateId
import fr.delphes.state.StateManager

class EventHandlerContext<EVENT>(
    @PublishedApi
    internal val outgoingEventProcessor: OutgoingEventProcessor,
    val event: EVENT,
    val stateManager: StateManager,
) {
    suspend fun executeOutgoingEvent(event: OutgoingEvent) {
        outgoingEventProcessor.processOutgoingEvent(event)
    }

    suspend fun executeOutgoingEvents(events: List<OutgoingEvent>) {
        events.forEach { event -> executeOutgoingEvent(event) }
    }

    inline fun <reified T : State> state(stateId: StateId<T>) = stateManager.getStateOrNull(stateId)

    inline fun <reified T> T.newActionContext() = EventHandlerContext(outgoingEventProcessor, this, stateManager)
}