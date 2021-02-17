package fr.delphes.connector.twitch

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.auth.AuthToken
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ConfigurationTwitchAccount(
    val authToken: AuthToken,
    val userName: String
) {
    @Transient
    val channel = TwitchChannel(userName)
}