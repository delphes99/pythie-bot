package fr.delphes

import kotlinx.serialization.Serializable

@Serializable
data class TwitchConfiguration(
    val clientId: String,
    val clientSecret: String,
    val botAccountCredential: ConfigurationTwitchAccount?,
    val listChannelCredential: List<ConfigurationTwitchAccount> = emptyList()
) {
    companion object {
        val empty = TwitchConfiguration("", "", null)
    }
}