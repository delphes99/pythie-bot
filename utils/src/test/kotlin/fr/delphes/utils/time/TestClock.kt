package fr.delphes.utils.time

import java.time.LocalDateTime

class TestClock(
    private val now: LocalDateTime
) : Clock {
    override fun now() = now
}