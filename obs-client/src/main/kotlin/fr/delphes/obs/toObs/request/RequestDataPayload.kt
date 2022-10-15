package fr.delphes.obs.toObs.request

import kotlinx.serialization.Serializable

@Serializable
sealed class RequestDataPayload {
    abstract val requestType: String
}


/*

@Serializable
data class SetSceneItemProperties(
    val item: String,
    @SerialName("scene-name")
    val sceneName: String? = null,
    val position: Position? = null,
    val visible: Boolean? = null,
    @SerialName("message-id")
    override val messageId: String = UUID.randomUUID().toString(),
) : RequestDataPayload() {
    @SerialName("request-type")
    override val requestType: String = "SetSceneItemProperties"

    @Transient
    override val responseType = SetSceneItemPropertiesResponse::class
}

@Serializable
data class Position(
    val x: Double,
    val y: Double,
)

@Serializable
data class SetSourceFilterVisibility(
    val sourceName: String,
    val filterName: String,
    val filterEnabled: Boolean,
    @SerialName("message-id")
    override val messageId: String = UUID.randomUUID().toString(),
) : RequestDataPayload() {
    @SerialName("request-type")
    override val requestType: String = "SetSourceFilterVisibility"

    @Transient
    override val responseType = SetSourceFilterVisibilityResponse::class
}*/