package fr.delphes.annotation.outgoingEvent

import fr.delphes.dynamicForm.FieldDescriptorMapper
import java.time.Duration

object DurationFixedTypeMapper : FieldDescriptorMapper<Duration> {
    override suspend fun map(value: String): Duration {
        return Duration.ZERO
    }
}