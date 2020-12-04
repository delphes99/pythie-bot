package fr.delphes.bot.state

import fr.delphes.twitch.api.user.User
import kotlinx.serialization.Serializable

@Serializable
data class UserMessage(
    val user: User,
    val text: String
)