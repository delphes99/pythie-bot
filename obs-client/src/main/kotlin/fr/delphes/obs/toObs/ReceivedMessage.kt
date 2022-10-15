package fr.delphes.obs.toObs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReceivedMessage(
    @SerialName("message-id")
    val messageId: String? = null,
    @SerialName("update-type")
    val event: String? = null,
) {
    fun isRequestResponse() = messageId != null
}