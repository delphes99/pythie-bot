package fr.delphes.state

import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import java.time.Duration
import java.time.LocalDateTime

class TimeState(
    override val id: StateId = StateId(),
    private var currentValue: LocalDateTime? = null,
    private val clock: Clock = SystemClock
) : State {
    fun getValue(): LocalDateTime? {
        return currentValue
    }

    fun put(value: LocalDateTime) {
        this.currentValue = value
    }

    fun putNow() {
        this.currentValue = clock.now()
    }

    fun hasMoreThan(duration: Duration): Boolean {
        return currentValue?.plus(duration)?.isBefore(clock.now()) ?: true
    }
}