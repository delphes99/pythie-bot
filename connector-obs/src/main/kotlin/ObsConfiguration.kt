package fr.delphes.connector.obs

import kotlinx.serialization.Serializable

@Serializable
data class ObsConfiguration(
    val url: String,
    val password: String?,
) {
    companion object {
        fun empty() : ObsConfiguration = ObsConfiguration("ws://localhost:4444", null)
    }
}
