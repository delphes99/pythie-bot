package fr.delphes.bot.state

import fr.delphes.twitch.api.user.UserName
import kotlinx.serialization.Serializable

@Serializable
data class UserMessage(
    val user: UserName,
    val text: String
)