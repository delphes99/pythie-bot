package fr.delphes.bot.event.outgoing

import fr.delphes.feature.OutgoingEventType

data class OutgoingEventRegistryEntry(
    val type: OutgoingEventType,
)