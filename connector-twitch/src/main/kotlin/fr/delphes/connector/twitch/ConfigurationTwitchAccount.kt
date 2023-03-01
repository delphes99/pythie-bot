package fr.delphes.connector.twitch

import fr.delphes.twitch.TwitchChannel
import fr.delphes.twitch.auth.AuthToken
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ConfigurationTwitchAccount(
    val authToken: AuthToken,
    val userName: ConfigurationTwitchAccountName
) {
    @Transient
    val channel = TwitchChannel(userName.userName)

    companion object {
        fun of(authToken: AuthToken, userName: String) = ConfigurationTwitchAccount(
            authToken,
            ConfigurationTwitchAccountName(userName)
        )
    }
}

@Serializable
@JvmInline
value class ConfigurationTwitchAccountName(val userName: String)