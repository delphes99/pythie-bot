package fr.delphes.annotation.outgoingEvent.createBuilder

import java.time.Duration

object DurationFixedTypeMapper : FieldDescriptorMapper<Duration> {
    override fun map(value: String): Duration {
        return Duration.ZERO
    }
}