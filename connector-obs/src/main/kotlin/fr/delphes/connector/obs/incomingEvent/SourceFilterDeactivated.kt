package fr.delphes.connector.obs.incomingEvent

import fr.delphes.annotation.incomingEvent.RegisterIncomingEvent
import fr.delphes.connector.obs.business.SourceFilter
import kotlinx.serialization.Serializable

@Serializable
@RegisterIncomingEvent
data class SourceFilterDeactivated(
    val filter: SourceFilter,
) : ObsIncomingEvent