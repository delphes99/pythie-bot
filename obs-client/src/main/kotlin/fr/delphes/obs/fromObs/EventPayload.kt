package fr.delphes.obs.fromObs

import fr.delphes.obs.fromObs.event.Event
import fr.delphes.obs.fromObs.event.EventType
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

@Serializable
@SerialName("5")
data class EventPayload(
    override val d: EventData
) : FromOBSMessagePayload()

@Serializable
data class EventData(
    val eventIntent: Long,
    val eventType: String,
    val eventData: JsonElement
) {
    @InternalSerializationApi
    fun toEvent(serializer: Json): Event? {
        return EventType.findByType(eventType)?.deserialize(eventData.toString(), serializer)
    }
}