package fr.delphes.bot.state

import fr.delphes.twitch.api.user.User

data class UserMessage(
    val user: User,
    val text: String
)