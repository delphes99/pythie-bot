package fr.delphes.obs.fromObs

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@SerialName("7")
data class RequestResponse(
    override val d: RequestResponseData
) : FromOBSMessagePayload()

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("requestType")
sealed class RequestResponseData {
    abstract val requestId: String
    abstract val requestStatus: RequestStatus
    abstract val responseData: Any
}

@Serializable
data class RequestStatus(
    val result: Boolean,
    val code: Long,
    val comment: String? = null
)
