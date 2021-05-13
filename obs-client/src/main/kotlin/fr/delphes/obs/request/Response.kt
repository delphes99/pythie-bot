package fr.delphes.obs.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class Response {
    abstract val messageId: String
    abstract val status: ResponseStatus?
    abstract val error: String?
}
@Serializable
data class AuthenticateResponse(
    @SerialName("message-id")
    override val messageId: String,
    override val status: ResponseStatus? = null,
    override val error: String? = null,
) : Response()

@Serializable
data class GetAuthRequiredResponse(
    @SerialName("message-id")
    override val messageId: String,
    override val status: ResponseStatus? = null,
    override val error: String? = null,
    val authRequired: Boolean,
    val challenge: String? = null,
    val salt: String? = null,
) : Response()

@Serializable
data class SetSceneItemPropertiesResponse(
    @SerialName("message-id")
    override val messageId: String,
    override val status: ResponseStatus? = null,
    override val error: String? = null,
) : Response()
