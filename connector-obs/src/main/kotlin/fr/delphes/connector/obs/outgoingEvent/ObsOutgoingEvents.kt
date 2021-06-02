package fr.delphes.connector.obs.outgoingEvent

import fr.delphes.bot.event.outgoing.OutgoingEvent
import fr.delphes.connector.obs.ObsConnector
import fr.delphes.connector.obs.business.SourceFilter
import fr.delphes.obs.request.Position
import fr.delphes.obs.request.SetSceneItemProperties
import fr.delphes.obs.request.SetSourceFilterVisibility
import kotlinx.serialization.InternalSerializationApi

@InternalSerializationApi
sealed class ObsOutgoingEvent : OutgoingEvent {
    abstract suspend fun executeOnObs(connector: ObsConnector)
}

@InternalSerializationApi
data class ChangeItemVisibility(
    val itemName: String,
    val visible: Boolean,
    val sceneName: String? = null,
) : ObsOutgoingEvent() {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            client.sendRequest(
                SetSceneItemProperties(
                    item = itemName,
                    sceneName = sceneName,
                    visible = visible
                )
            )
        }
    }
}

@InternalSerializationApi
data class ChangeItemPosition(
    val itemName: String,
    val x: Double,
    val y: Double,
    val sceneName: String? = null,
) : ObsOutgoingEvent() {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            client.sendRequest(
                SetSceneItemProperties(
                    item = itemName,
                    sceneName = sceneName,
                    position = Position(x, y)
                )
            )
        }
    }
}

@InternalSerializationApi
data class ActivateFilter(
    val filter: SourceFilter,
) : ObsOutgoingEvent() {
    override suspend fun executeOnObs(connector: ObsConnector) {
        connector.connected {
            client.sendRequest(
                SetSourceFilterVisibility(
                    sourceName = filter.sourceName,
                    filterName = filter.filterName,
                    filterEnabled = true
                )
            )
        }
    }
}

@InternalSerializationApi
data class DeactivateFilter(
    val filter: SourceFilter,
) : ObsOutgoingEvent() {
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
