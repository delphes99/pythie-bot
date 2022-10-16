package fr.delphes.obs.fromObs.event

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("eventType")
sealed class EventData {
    abstract val eventIntent: Long
    abstract val eventType: String
    abstract val eventData: Any
}