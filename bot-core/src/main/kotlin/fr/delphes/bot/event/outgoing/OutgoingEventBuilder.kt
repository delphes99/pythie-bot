package fr.delphes.bot.event.outgoing

import fr.delphes.feature.OutgoingEventBuilderDescription

interface OutgoingEventBuilder {
    suspend fun build(): OutgoingEvent
    suspend fun description(): OutgoingEventBuilderDescription
}
