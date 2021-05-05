package fr.delphes.connector.obs

import kotlinx.serialization.Serializable

@Serializable
data class ObsConfiguration(
    val host: String,
    val port: Int,
    val password: String?,
) {
    companion object {
        fun empty(): ObsConfiguration = ObsConfiguration("localhost", 4444, null)
    }
}
