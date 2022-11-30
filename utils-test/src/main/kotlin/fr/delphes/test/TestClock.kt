package fr.delphes.test

import fr.delphes.utils.time.Clock
import java.time.LocalDateTime

class TestClock(
    private val now: LocalDateTime
) : Clock {
    override fun now() = now
}