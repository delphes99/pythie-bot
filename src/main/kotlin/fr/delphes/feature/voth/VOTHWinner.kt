package fr.delphes.feature.voth

import fr.delphes.User
import java.time.LocalDateTime

data class VOTHWinner(
    val user: User,
    val since: LocalDateTime
)