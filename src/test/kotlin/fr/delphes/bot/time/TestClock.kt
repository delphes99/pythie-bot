package fr.delphes.bot.time

import java.time.LocalDateTime

class TestClock(
    private val now: LocalDateTime
) : Clock {
    override fun now() = now
}