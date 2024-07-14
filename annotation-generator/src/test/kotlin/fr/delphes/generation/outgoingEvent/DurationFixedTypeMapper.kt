package fr.delphes.generation.outgoingEvent

import fr.delphes.generation.dynamicForm.FieldDescriptorMapper
import java.time.Duration

object DurationFixedTypeMapper : FieldDescriptorMapper<Duration> {
    override fun mapFromDto(value: String): Duration {
        return Duration.ZERO
    }

    override fun mapToDto(value: Duration): String {
        return Duration.ZERO.toString()
    }
}