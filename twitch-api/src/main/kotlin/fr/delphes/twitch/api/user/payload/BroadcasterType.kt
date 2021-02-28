package fr.delphes.twitch.api.user.payload

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class BroadcasterType {
    @SerialName("partner")
    PARTNER,
    @SerialName("affiliate")
    AFFILIATE,
    @SerialName("")
    DEFAULT;
}