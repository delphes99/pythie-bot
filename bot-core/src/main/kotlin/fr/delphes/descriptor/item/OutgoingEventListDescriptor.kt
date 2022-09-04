package fr.delphes.descriptor.item

import fr.delphes.bot.event.outgoing.OutgoingEventBuilder
import fr.delphes.descriptor.Descriptor
import fr.delphes.descriptor.registry.ToDescriptorMapper
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
@SerialName("description-outgoing-event-list")
data class OutgoingEventListDescriptor(
    val name: String,
    val value: List<Descriptor>? = null
) : ItemDescriptor() {
    companion object {
        fun of(
            name: String,
            events: List<OutgoingEventBuilder>,
            mapper: ToDescriptorMapper
        ): OutgoingEventListDescriptor {
            return OutgoingEventListDescriptor(
                name,
                events.mapNotNull { event -> mapper.descriptorOf(event, mapper) }
            )
        }
    }
}