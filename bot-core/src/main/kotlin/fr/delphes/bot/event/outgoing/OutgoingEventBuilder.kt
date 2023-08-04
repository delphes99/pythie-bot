package fr.delphes.bot.event.outgoing

import fr.delphes.feature.OutgoingEventBuilderDescription

interface OutgoingEventBuilder {
    fun build(): OutgoingEvent
    fun description(): OutgoingEventBuilderDescription
}
