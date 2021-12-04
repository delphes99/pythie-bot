package fr.delphes.bot.connector.state.endpoint

import kotlinx.serialization.Serializable

@Serializable
data class ConnectorStatus(
    val name: String,
    val status: ConnectorStatusEnum
)