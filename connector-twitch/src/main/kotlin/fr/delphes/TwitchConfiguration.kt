package fr.delphes

import kotlinx.serialization.Serializable

@Serializable
class TwitchConfiguration(
    val clientId: String,
    val clientSecret: String
) {
    companion object {
        val empty = TwitchConfiguration("", "")
    }
}