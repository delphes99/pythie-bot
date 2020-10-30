package fr.delphes.bot.state

import fr.delphes.User

data class UserMessage(
    val user: User,
    val text: String
)