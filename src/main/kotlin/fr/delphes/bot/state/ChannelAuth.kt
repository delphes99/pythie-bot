package fr.delphes.bot.state

import kotlinx.serialization.Serializable

@Serializable
data class ChannelAuth(
    val access_token: String,
    val refresh_token: String,
    val scope: List<String> = emptyList()
) {
    companion object {
        fun empty(): ChannelAuth {
            return ChannelAuth("", "", emptyList())
        }
    }
}
