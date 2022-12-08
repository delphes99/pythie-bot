package fr.delphes.state.state

import fr.delphes.state.State
import fr.delphes.state.StateId
import fr.delphes.state.StateManager
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

    companion object {
        fun withClockFrom(
            stateManager: StateManager,
            id: StateId = StateId(),
            currentValue: LocalDateTime? = null
        ): TimeState {
            val clock = stateManager.get<ClockState>(ClockState.STATE_ID)?.clock
                ?: throw IllegalStateException("Clock state required")
            return TimeState(
                id = id,
                currentValue = currentValue,
                clock = clock
            )
        }
    }
}