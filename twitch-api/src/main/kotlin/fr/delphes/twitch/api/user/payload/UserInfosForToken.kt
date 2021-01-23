package fr.delphes.twitch.api.user.payload

import kotlinx.serialization.Serializable

@Serializable
data class UserInfosForToken(
    val preferred_username: String
)