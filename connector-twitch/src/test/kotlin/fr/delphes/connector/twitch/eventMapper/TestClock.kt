package fr.delphes.connector.twitch.eventMapper

import fr.delphes.utils.time.Clock
import java.time.LocalDateTime

//TODO move to shared package (duplicate code)
class TestClock(
    private val now: LocalDateTime
) : Clock {
    override fun now() = now
}