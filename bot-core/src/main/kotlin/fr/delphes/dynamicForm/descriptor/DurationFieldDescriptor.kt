package fr.delphes.dynamicForm.descriptor

import fr.delphes.utils.serialization.DurationSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Duration

@SerialName("DURATION")
@Serializable
data class DurationFieldDescriptor(
    override val fieldName: String,
    override val description: String,
    @Serializable(with = DurationSerializer::class)
    override val value: Duration?,
) : FieldDescriptor()