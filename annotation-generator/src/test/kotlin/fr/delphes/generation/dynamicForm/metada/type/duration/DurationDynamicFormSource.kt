package fr.delphes.generation.dynamicForm.metada.type.duration

import fr.delphes.annotation.dynamicForm.DynamicForm
import fr.delphes.annotation.dynamicForm.FieldDescription
import java.time.Duration

@DynamicForm("serializeName")
class DurationDynamicFormSource(
    @FieldDescription("duration description")
    val myField: Duration,
)