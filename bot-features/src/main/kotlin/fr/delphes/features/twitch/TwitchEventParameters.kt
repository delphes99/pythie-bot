package fr.delphes.features.twitch

import fr.delphes.bot.OutgoingEventProcessor
import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.twitch.incomingEvent.TwitchIncomingEvent
import fr.delphes.state.State
import fr.delphes.state.StateId
import fr.delphes.state.StateManager

class TwitchEventParameters<T: TwitchIncomingEvent>(
    private val outgoingEventProcessor: OutgoingEventProcessor,
    val event: T,
    @PublishedApi
    internal val stateManager: StateManager
) {
    suspend fun executeOutgoingEvent(event: OutgoingEvent) {
        outgoingEventProcessor.processOutgoingEvent(event)
    }

    inline fun <reified U: State> state(stateId: StateId) = stateManager.get<U>(stateId)
}