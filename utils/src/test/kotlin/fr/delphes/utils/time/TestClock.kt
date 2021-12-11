package fr.delphes.utils.time

import java.time.LocalDateTime

//TODO move to shared package (duplicate code)
class TestClock(
    private val now: LocalDateTime
) : Clock {
    override fun now() = now
}