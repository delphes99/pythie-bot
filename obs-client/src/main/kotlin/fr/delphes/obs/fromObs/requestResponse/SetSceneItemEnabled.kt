package fr.delphes.obs.fromObs.requestResponse

import fr.delphes.obs.fromObs.RequestStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("SetSceneItemEnabled")
data class SetSceneItemEnabled(
    override val requestId: String,
    override val requestStatus: RequestStatus
): RequestResponseData() {
    override val responseData: EmptyResponseData? = null
    override val requestType: String = "SetSceneItemEnabled"
}