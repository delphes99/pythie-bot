package fr.delphes.state.state

import fr.delphes.state.State
import fr.delphes.state.StateId
import fr.delphes.utils.time.Clock
import fr.delphes.utils.time.SystemClock
import java.time.LocalDateTime

class ClockState(val clock: Clock = SystemClock) : State {
    override val id = ID

    fun getValue(): LocalDateTime {
        return clock.now()
    }

    companion object {
        val ID = StateId.from<ClockState>("clock")
    }
}
