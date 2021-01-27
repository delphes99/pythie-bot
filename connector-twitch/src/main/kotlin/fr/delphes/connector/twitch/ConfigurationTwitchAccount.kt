package fr.delphes.connector.twitch

import fr.delphes.twitch.auth.AuthToken
import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationTwitchAccount(
    val authToken: AuthToken,
    val userName: String
)