package fr.delphes

import fr.delphes.twitch.auth.AuthToken
import kotlinx.serialization.Serializable

@Serializable
data class TwitchConfiguration(
    val clientId: String,
    val clientSecret: String,
    val botAccountCredential: AuthToken
) {
    companion object {
        val empty = TwitchConfiguration("", "", AuthToken.empty)
    }
}