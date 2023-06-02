package fr.delphes.bot.connector

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class ConnectorType(val name: String)