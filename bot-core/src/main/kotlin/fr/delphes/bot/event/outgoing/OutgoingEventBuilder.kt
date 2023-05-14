package fr.delphes.bot.event.outgoing

import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.state.StateManager

interface OutgoingEventBuilder {
    suspend fun build(stateManager: StateManager): OutgoingEvent
    fun description(): OutgoingEventBuilderDescription
}
