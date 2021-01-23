package fr.delphes

import fr.delphes.twitch.auth.AuthToken
import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationTwitchAccount(
    val authToken: AuthToken,
    val userName: String
)