package fr.delphes.dynamicForm.descriptor

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.feature.OutgoingEventBuilderDescription
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@SerialName("OUTGOING_EVENTS")
@Serializable
data class OutgoingEventsFieldDescriptor(
    override val fieldName: String,
    override val description: String,
    override val value: List<OutgoingEventBuilderDescription> = emptyList(),
) : FieldDescriptor() {
    companion object {
        suspend fun fromBuilders(
            fieldName: String,
            description: String,
            builders: List<OutgoingEventBuilder>,
        ) = OutgoingEventsFieldDescriptor(
            fieldName = fieldName,
            description = description,
            value = builders.map { it.description() },
        )
    }
}