package fr.delphes.annotation.outgoingEvent

import java.time.Duration

class DurationFixedTypeMapper : FieldDescriptorMapper<Duration> {
    override fun map(value: Duration): String {
        return ""
    }
}