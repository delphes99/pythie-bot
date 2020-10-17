package fr.delphes.bot.state

import fr.delphes.bot.twitch.game.Game
import java.time.LocalDateTime

data class CurrentStream(
    val title: String,
    val start: LocalDateTime,
    val game: Game
)