package fr.delphes.twitch.api.streams

import fr.delphes.twitch.api.games.Game
import java.time.LocalDateTime

data class Stream(
    val id: String,
    val title: String,
    val start: LocalDateTime,
    val game: Game
)