package fr.delphes.bot.event.outgoing

import fr.delphes.feature.OutgoingEventType

class OutgoingEventBuilderDefinition(
    val type: OutgoingEventType,
    private val newBuilderProvider: () -> OutgoingEventBuilder,
) {
    fun providerEmptyDescription() = newBuilderProvider().description()
}
