package fr.delphes.obs.fromObs.event

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("eventType")
sealed class EventData {
    abstract val eventIntent: Long
    abstract val eventType: String
    abstract val eventData: Any?
}