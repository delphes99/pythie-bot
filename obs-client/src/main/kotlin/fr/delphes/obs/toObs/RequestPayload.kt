package fr.delphes.obs.toObs

import fr.delphes.obs.toObs.request.RequestDataPayload
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import java.util.UUID

@Serializable
data class RequestPayload(
    val requestType: String,
    val requestId: String,
    val requestData: JsonElement,
) : ToObsMessagePayload {
    companion object {
        fun toToObsMessagePayload(requestPayload: RequestDataPayload, serializer: Json): RequestPayload {
            return RequestPayload(
                requestType = requestPayload.requestType,
                requestId = UUID.randomUUID().toString(),
                requestData = serializer.encodeToJsonElement(requestPayload)
            )
        }
    }
}
