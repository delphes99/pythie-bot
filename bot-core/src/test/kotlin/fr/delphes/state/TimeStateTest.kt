package fr.delphes.state

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class TimeStateTest : ShouldSpec({
    val A_DATE = LocalDateTime.of(2021, 1, 1, 0, 0, 0)
    should("should return null if state does not exist") {
        val state = TimeState()

        state.getValue().shouldBeNull()
    }

    should("should return saved state") {
        val state = TimeState()
        state.put(A_DATE)

        state.getValue() shouldBe A_DATE
    }
})
