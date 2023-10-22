package fr.delphes.bot.event.outgoing

import fr.delphes.feature.OutgoingEventType

class OutgoingEventRegistry(
    private val entries: List<OutgoingEventRegistryEntry>,
) {
    fun types(): List<OutgoingEventType> = entries.map(OutgoingEventRegistryEntry::type)
}