package fr.delphes.feature

import fr.delphes.User
import java.time.LocalDateTime

data class VOTHWinner(
    val user: User,
    val since: LocalDateTime
)