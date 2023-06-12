package fr.delphes.connector.obs.incomingEvent

import fr.delphes.annotation.RegisterIncomingEvent
import fr.delphes.connector.obs.business.SourceFilter
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
data class SourceFilterActivated(
    val filter: SourceFilter,
) : ObsIncomingEvent