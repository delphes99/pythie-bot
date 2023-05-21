package fr.delphes.state.state

import fr.delphes.state.State
import fr.delphes.state.StateId
import fr.delphes.state.StateIdQualifier
import fr.delphes.state.StateManager
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import java.time.Duration
import java.time.LocalDateTime

class TimeState(
    qualifier: StateIdQualifier = StateIdQualifier(),
    private var currentValue: LocalDateTime? = null,
    private val clock: Clock = SystemClock,
) : State {
    override val id = StateId.from<TimeState>(qualifier)

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
        fun id(qualifier: String) = StateId.from<TimeState>(qualifier)

        fun withClockFrom(
            stateManager: StateManager,
            id: StateIdQualifier = StateIdQualifier(),
            currentValue: LocalDateTime? = null,
        ): TimeState {
            val clock = stateManager.getStateOrNull(ClockState.ID)?.clock
                ?: throw IllegalStateException("Clock state required")
            return TimeState(
                qualifier = id,
                currentValue = currentValue,
                clock = clock
            )
        }
    }
}