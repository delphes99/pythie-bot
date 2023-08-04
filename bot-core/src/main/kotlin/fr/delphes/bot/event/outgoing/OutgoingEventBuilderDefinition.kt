package fr.delphes.bot.event.outgoing

import fr.delphes.feature.OutgoingEventType

class OutgoingEventBuilderDefinition(
    val type: OutgoingEventType,
    private val newBuilderProvider: () -> LegacyOutgoingEventBuilder,
) {
    fun providerEmptyDescription() = newBuilderProvider().description()
}
