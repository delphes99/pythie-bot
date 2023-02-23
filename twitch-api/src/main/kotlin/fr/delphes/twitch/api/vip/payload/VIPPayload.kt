package fr.delphes.twitch.api.vip.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VIPPayload(
    @SerialName("user_id")
    val userId: String,
    @SerialName("user_name")
    val userName: String,
    @SerialName("user_login")
    val userLogin: String
)
