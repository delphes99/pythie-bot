package fr.delphes.connector.obs.incomingEvent

import fr.delphes.connector.obs.business.SourceFilter
import kotlinx.serialization.Serializable

@Serializable
data class SourceFilterActivated(
    val filter: SourceFilter,
) : ObsIncomingEvent