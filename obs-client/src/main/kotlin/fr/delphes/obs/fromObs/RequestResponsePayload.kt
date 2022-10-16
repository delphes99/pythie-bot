package fr.delphes.obs.fromObs

import fr.delphes.obs.fromObs.requestResponse.RequestResponseData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("7")
data class RequestResponse(
    override val d: RequestResponseData
) : FromOBSMessagePayload()

@Serializable
data class RequestStatus(
    val result: Boolean,
    val code: Long,
    val comment: String? = null
)
