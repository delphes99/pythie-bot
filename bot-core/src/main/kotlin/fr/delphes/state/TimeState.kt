package fr.delphes.state

import java.time.LocalDateTime

class TimeState(
    override val id: StateId = StateId(),
    private var currentValue: LocalDateTime? = null
) : State {
    fun getValue(): LocalDateTime? {
        return currentValue
    }

    fun put(value: LocalDateTime) {
        this.currentValue = value
    }

}