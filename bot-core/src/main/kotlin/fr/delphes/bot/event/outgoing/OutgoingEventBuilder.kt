package fr.delphes.bot.event.outgoing

import fr.delphes.feature.OutgoingEventBuilderDescription
import fr.delphes.state.StateProvider

interface OutgoingEventBuilder {
    suspend fun build(stateProvider: StateProvider): OutgoingEvent
    suspend fun description(): OutgoingEventBuilderDescription
}
