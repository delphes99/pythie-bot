package fr.delphes.connector.obs

import fr.delphes.bot.connector.ConnectorConfiguration
import kotlinx.serialization.Serializable

@Serializable
data class ObsConfiguration(
    val host: String,
    val port: Int,
    val password: String?,
): ConnectorConfiguration {
    companion object {
        fun empty(): ObsConfiguration = ObsConfiguration("localhost", 4444, null)
    }
}
