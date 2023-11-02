package fr.delphes.annotation.outgoingEvent

import fr.delphes.annotation.outgoingEvent.createBuilder.FieldDescriptorMapper
import fr.delphes.state.StateProvider
import java.time.Duration

object DurationFixedTypeMapper : FieldDescriptorMapper<Duration> {
    override suspend fun map(value: String, stateProvider: StateProvider): Duration {
        return Duration.ZERO
    }
}