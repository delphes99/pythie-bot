package fr.delphes.bot.event.builder

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.state.StateManager

interface OutgoingEventBuilder {
    suspend fun build(stateManager: StateManager): OutgoingEvent
    fun description(): OutgoingEventBuilderDescription
}
