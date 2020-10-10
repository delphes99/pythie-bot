package fr.delphes.feature.statistics

import fr.delphes.User

data class UserMessage(
    val user: User,
    val text: String
)