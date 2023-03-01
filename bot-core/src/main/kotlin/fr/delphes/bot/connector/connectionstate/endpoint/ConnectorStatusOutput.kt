package fr.delphes.bot.connector.connectionstate.endpoint

import kotlinx.serialization.Serializable

@Serializable
data class ConnectorStatusOutput(
    val name: String,
    val status: ConnectorStatusEnum,
    val subStatus: List<ConnectorStatusOutput> = emptyList(),
)