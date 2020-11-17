package fr.delphes.twitch.model

import java.time.LocalDateTime

data class Stream(
    val title: String,
    val start: LocalDateTime,
    val game: Game
)