package fr.delphes.annotation.outgoingEvent

import java.time.Duration

object DurationFixedTypeMapper : FieldDescriptorMapper<Duration> {
    override fun map(value: String): Duration {
        return Duration.ZERO
    }
}