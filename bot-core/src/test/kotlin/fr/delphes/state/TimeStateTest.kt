package fr.delphes.state

import fr.delphes.state.state.TimeState
import fr.delphes.test.TestClock
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.LocalDateTime

class TimeStateTest : ShouldSpec({
    should("return null if state does not exist") {
        val state = TimeState()

        state.getValue().shouldBeNull()
    }
    should("return saved state") {
        val state = TimeState()

        state.put(A_DATE)

        state.getValue() shouldBe A_DATE
    }
    should("put now") {
        val state = TimeState(clock = TestClock(A_DATE))

        state.putNow()

        state.getValue() shouldBe A_DATE
    }
    should("has more than duration return true if state is not defined") {
        val state = TimeState(clock = TestClock(A_DATE))

        state.hasMoreThan(Duration.ofHours(1)) shouldBe true
    }
    should("has more than duration return true if time between state and now is greater than duration") {
        val state = TimeState(clock = TestClock(A_DATE))

        state.put(A_DATE.minusHours(2))

        state.hasMoreThan(Duration.ofHours(1)) shouldBe true
    }
    should("has more than duration return false if time between state and now is less than duration") {
        val state = TimeState(clock = TestClock(A_DATE))

        state.put(A_DATE.minusHours(2))

        state.hasMoreThan(Duration.ofHours(3)) shouldBe false
    }
}) {
    companion object {
        private val A_DATE = LocalDateTime.of(2021, 1, 1, 0, 0, 0)
    }
}
