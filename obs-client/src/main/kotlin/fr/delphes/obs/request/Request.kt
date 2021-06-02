package fr.delphes.obs.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID
import kotlin.reflect.KClass

@Serializable
sealed class Request {
    abstract val messageId: String
    abstract val requestType: String
    abstract val responseType: KClass<*>
}

@Serializable
class GetAuthRequired(
    @SerialName("message-id")
    override val messageId: String = UUID.randomUUID().toString(),
) : Request() {
    @SerialName("request-type")
    override val requestType = "GetAuthRequired"

    @Transient
    override val responseType = GetAuthRequiredResponse::class
}

@Serializable
data class Authenticate(
    val auth: String,
    @SerialName("message-id")
    override val messageId: String = UUID.randomUUID().toString()
) : Request() {
    @SerialName("request-type")
    override val requestType: String = "Authenticate"

    @Transient
    override val responseType = AuthenticateResponse::class
}

@Serializable
data class SetSceneItemProperties(
    val item: String,
    @SerialName("scene-name")
    val sceneName: String? = null,
    val position: Position? = null,
    val visible: Boolean? = null,
    @SerialName("message-id")
    override val messageId: String = UUID.randomUUID().toString(),
) : Request() {
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
) : Request() {
    @SerialName("request-type")
    override val requestType: String = "SetSourceFilterVisibility"

    @Transient
    override val responseType = SetSourceFilterVisibilityResponse::class
}