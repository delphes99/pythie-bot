package fr.delphes.bot.connector.status

import fr.delphes.bot.connector.state.endpoint.ConnectorStatusEnum
import fr.delphes.bot.connector.state.endpoint.ConnectorStatusOutput

data class ConnectorStatus(
    val subStatus: Map<ConnectorConnectionName, ConnectorConnectionStatus>
) {
    constructor(vararg subStatus: Pair<ConnectorConnectionName, ConnectorConnectionStatus>) : this(mapOf(*subStatus))

    val status: ConnectorConnectionStatus get() {
        return if(subStatus.values.distinct().size == 1) {
            subStatus.values.first()
        } else {
            ConnectorConnectionStatus.Mixed
        }
    }
}

fun toOutput(connectorName: String, connectorStatus: ConnectorStatus): ConnectorStatusOutput {
    return ConnectorStatusOutput(
        connectorName,
        connectorStatus.status.toConnectorStatusEnum(),
        connectorStatus.subStatus.map { (connectionName, status) -> ConnectorStatusOutput(connectionName, status.toConnectorStatusEnum()) }
    )
}

fun ConnectorConnectionStatus.toConnectorStatusEnum(): ConnectorStatusEnum {
    return when (this) {
        ConnectorConnectionStatus.Configured -> ConnectorStatusEnum.configured
        ConnectorConnectionStatus.Connected -> ConnectorStatusEnum.connected
        ConnectorConnectionStatus.Connecting -> ConnectorStatusEnum.connecting
        ConnectorConnectionStatus.Disconnecting -> ConnectorStatusEnum.disconnecting
        ConnectorConnectionStatus.InError -> ConnectorStatusEnum.inError
        ConnectorConnectionStatus.NotConfigured -> ConnectorStatusEnum.unconfigured
        ConnectorConnectionStatus.Mixed -> ConnectorStatusEnum.mixed
    }
}