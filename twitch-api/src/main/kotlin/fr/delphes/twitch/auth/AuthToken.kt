package fr.delphes.twitch.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthToken(
    val access_token: String,
    val refresh_token: String? = null,
    val scope: List<String> = emptyList()
) {
    companion object {
        fun empty(): AuthToken {
            return AuthToken("", "", emptyList())
        }
    }
}
