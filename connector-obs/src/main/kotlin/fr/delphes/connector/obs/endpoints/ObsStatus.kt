package fr.delphes.connector.obs.endpoints

import kotlinx.serialization.Serializable

@Serializable
data class ObsStatus(
    val status: ObsStatusEnum
)