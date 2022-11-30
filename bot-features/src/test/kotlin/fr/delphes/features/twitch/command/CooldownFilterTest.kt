package fr.delphes.features.twitch.command

import fr.delphes.test.TestClock
import fr.delphes.features.IncomingEventStub
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.time.Duration
import java.time.LocalDateTime

internal class CooldownFilterTest : ShouldSpec({
    val clock = TestClock(NOW)

    should("true if no cooldown") {
        CooldownFilter(null, clock).isApplicable(IncomingEventStub, NewTwitchCommandState()).shouldBeTrue()
    }

    should("true if no last call in state") {
        CooldownFilter(COOLDOWN, clock).isApplicable(IncomingEventStub, NewTwitchCommandState()).shouldBeTrue()
    }

    should("true if cooldown expired") {
        val cooldownExpired = NOW.minus(COOLDOWN).minusSeconds(5)

        CooldownFilter(COOLDOWN, clock)
            .isApplicable(IncomingEventStub, NewTwitchCommandState(cooldownExpired))
            .shouldBeTrue()
    }

    should("false if in cooldown") {
        val inCooldown = NOW.minus(COOLDOWN).plusSeconds(5)

        CooldownFilter(COOLDOWN, clock)
            .isApplicable(IncomingEventStub, NewTwitchCommandState(inCooldown))
            .shouldBeFalse()
    }
}) {
    companion object {
        private val NOW = LocalDateTime.of(2020, 1, 1, 0, 0)
        private val COOLDOWN = Duration.ofSeconds(10)
    }
}