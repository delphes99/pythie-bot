package fr.delphes.obs.toObs

import fr.delphes.obs.message.RpcVersion
import kotlinx.serialization.Serializable

@Serializable
data class IdentifyPayload(
    val authentication: String?,
    val rpcVersion: RpcVersion = 1
): ToObsMessagePayload