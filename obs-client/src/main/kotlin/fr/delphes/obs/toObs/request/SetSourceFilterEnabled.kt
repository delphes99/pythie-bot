package fr.delphes.obs.toObs.request

import kotlinx.serialization.Serializable

@Serializable
data class SetSourceFilterEnabled(
    val sourceName: String,
    val filterName: String,
    val filterEnabled: Boolean
) : RequestDataPayload() {
    override val requestType = "SetSourceFilterEnabled"
}