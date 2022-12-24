package fr.delphes.features.twitch

import fr.delphes.bot.OutgoingEventProcessor
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.state.State
import fr.delphes.state.StateManager
import fr.delphes.state.StateId

class TwitchEventParameters<T: TwitchIncomingEvent>(
    private val outgoingEventProcessor: OutgoingEventProcessor,
    val event: T,
    @PublishedApi
    internal val stateManager: StateManager
) {
    suspend fun executeOutgoingEvent(event: OutgoingEvent) {
        outgoingEventProcessor.processOutgoingEvent(event)
    }

    inline fun <reified T: State> state(stateId: StateId<T>) = stateManager.getState(stateId)
}