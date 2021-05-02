package fr.delphes.obs.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID
import kotlin.reflect.KClass

sealed class Request{
    abstract val messageId: String
    abstract val requestType: String
    @Transient
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
): Request() {
    @SerialName("request-type")
    override val requestType: String = "Authenticate"
    @Transient
    override val responseType = AuthenticateResponse::class
}