package fr.delphes.obs.fromObs

import fr.delphes.obs.fromObs.event.EventData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("5")
data class EventPayload(
    override val d: EventData
) : FromOBSMessagePayload()

