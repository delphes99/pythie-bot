package fr.delphes.obs.fromObs.requestResponse

import fr.delphes.obs.fromObs.RequestStatus
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("requestType")
sealed class RequestResponseData {
    abstract val requestId: String
    abstract val requestType: String
    abstract val requestStatus: RequestStatus
    abstract val responseData: Any?
}