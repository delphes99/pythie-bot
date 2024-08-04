package fr.delphes.generation.dynamicForm.metadata.type.duration

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.FieldDescription
import java.time.Duration

@DynamicForm("serializeName")
class DurationDynamicFormSource(
    @FieldDescription("duration description")
    val myField: Duration,
)