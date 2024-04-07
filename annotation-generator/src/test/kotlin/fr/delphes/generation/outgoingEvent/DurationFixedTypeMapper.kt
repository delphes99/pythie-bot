package fr.delphes.generation.outgoingEvent

import fr.delphes.generation.dynamicForm.FieldDescriptorMapper
import java.time.Duration

object DurationFixedTypeMapper : FieldDescriptorMapper<Duration> {
    override suspend fun map(value: String): Duration {
        return Duration.ZERO
    }
}