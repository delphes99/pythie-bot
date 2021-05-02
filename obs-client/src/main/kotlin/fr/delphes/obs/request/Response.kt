package fr.delphes.obs.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class Response

@Serializable
data class AuthenticateResponse(
    @SerialName("message-id")
    val messageId: String,
    val status: ResponseStatus? = null,
    val error: String? = null,
) : Response()

@Serializable
data class GetAuthRequiredResponse(
    @SerialName("message-id")
    val messageId: String,
    val status: ResponseStatus? = null,
    val error: String? = null,
    val authRequired: Boolean,
    val challenge: String? = null,
    val salt: String? = null,
) : Response()