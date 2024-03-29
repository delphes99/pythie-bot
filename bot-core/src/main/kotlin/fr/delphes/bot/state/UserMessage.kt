package fr.delphes.bot.state

import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
@Deprecated("twitch specific statistics")
data class UserMessage(
    val user: UserName,
    val text: String,
)