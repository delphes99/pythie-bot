package fr.delphes.bot.util.time

import fr.delphes.utils.time.Clock
import java.time.LocalDateTime

class TestClock(
    private val now: LocalDateTime
) : Clock {
    override fun now() = now
}