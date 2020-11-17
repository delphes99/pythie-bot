package fr.delphes.twitch.payload

import kotlinx.serialization.Serializable

@Serializable
data class ListenToData(
    val topics: List<String>,
    val auth_token: String
) {
    constructor(auth_token: String, vararg topics: String): this(listOf(*topics), auth_token)
}