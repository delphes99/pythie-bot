package fr.delphes.bot.event.builder

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.feature.OutgoingEventBuilderDescription

interface OutgoingEventBuilder {
    fun build(): OutgoingEvent
    fun description(): OutgoingEventBuilderDescription
}
