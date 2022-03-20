package fr.delphes.features.twitch.command

import fr.delphes.features.IncomingEventStub
import fr.delphes.features.TestClock
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

internal class CooldownFilterTest {
    private val clock = TestClock(NOW)

    @Test
    internal fun `true if no cooldown`() {
        CooldownFilter(null, clock).isApplicable(IncomingEventStub, NewTwitchCommandState()).shouldBeTrue()
    }

    @Test
    internal fun `true if no last call in state`() {
        CooldownFilter(COOLDOWN, clock).isApplicable(IncomingEventStub, NewTwitchCommandState()).shouldBeTrue()
    }

    @Test
    internal fun `true if cooldown expired`() {
        val cooldownExpired = NOW.minus(COOLDOWN).minusSeconds(5)

        CooldownFilter(COOLDOWN, clock)
            .isApplicable(IncomingEventStub, NewTwitchCommandState(cooldownExpired))
            .shouldBeTrue()
    }

    @Test
    internal fun `false if in cooldown`() {
        val inCooldown = NOW.minus(COOLDOWN).plusSeconds(5)

        CooldownFilter(COOLDOWN, clock)
            .isApplicable(IncomingEventStub, NewTwitchCommandState(inCooldown))
            .shouldBeFalse()
    }

    companion object {
        private val NOW = LocalDateTime.of(2020, 1, 1, 0, 0)
        private val COOLDOWN = Duration.ofSeconds(10)
    }
}