package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.connector.obs.ObsConnector
import fr.delphes.connector.obs.business.SourceFilter
import fr.delphes.obs.request.SetSourceFilterVisibility
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi
data class DeactivateFilter(
    val filter: SourceFilter,
) : ObsOutgoingEvent {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            client.sendRequest(
                SetSourceFilterVisibility(
                    sourceName = filter.sourceName,
                    filterName = filter.filterName,
                    filterEnabled = false
                )
            )
        }
    }
}