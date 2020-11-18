package fr.delphes.bot.state

import fr.delphes.twitch.model.User

data class UserMessage(
    val user: User,
    val text: String
)