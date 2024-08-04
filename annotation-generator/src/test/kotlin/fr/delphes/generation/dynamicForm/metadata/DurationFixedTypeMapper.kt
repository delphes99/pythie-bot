package fr.delphes.generation.dynamicForm.metadata

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