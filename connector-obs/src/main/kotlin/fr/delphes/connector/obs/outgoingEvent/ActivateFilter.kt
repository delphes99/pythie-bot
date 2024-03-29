package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.connector.obs.ObsConnector
import fr.delphes.connector.obs.business.SourceFilter
import fr.delphes.obs.toObs.request.SetSourceFilterEnabled

data class ActivateFilter(
    val filter: SourceFilter,
) : ObsOutgoingEvent {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            client.sendRequest(
                SetSourceFilterEnabled(
                    filter.sourceName,
                    filter.filterName,
                    true
                )
            )
        }
    }
}