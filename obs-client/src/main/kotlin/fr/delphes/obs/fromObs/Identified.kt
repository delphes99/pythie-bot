package fr.delphes.obs.fromObs

import fr.delphes.obs.message.RpcVersion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("2")
data class Identified(
    override val d: IdentifiedData
) : FromOBSMessagePayload()

@Serializable
data class IdentifiedData(
    val negotiatedRpcVersion: RpcVersion
)
