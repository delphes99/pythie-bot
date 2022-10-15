package fr.delphes.obs.fromObs

import fr.delphes.obs.message.RpcVersion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("0")
data class Hello(
    override val d: HelloPayloadData
) : FromOBSMessagePayload()

@Serializable
data class HelloPayloadData(
    val obsWebSocketVersion: String,
    val rpcVersion: RpcVersion,
    val authentication: Authentication? = null
)

@Serializable
data class Authentication(
    val challenge: String,
    val salt: String,
)